package technology.sola.engine.platform.javafx;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.FontAssetLoader;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheetAssetLoader;
import technology.sola.engine.assets.input.ControlsConfigAssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.SolaPlatformIdentifier;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.input.MouseWheelEvent;
import technology.sola.engine.networking.rest.JavaRestClient;
import technology.sola.engine.networking.socket.JavaSocketClient;
import technology.sola.engine.platform.javafx.assets.JavaFxPathUtils;
import technology.sola.engine.platform.javafx.assets.audio.JavaFxAudioClipAssetLoader;
import technology.sola.engine.platform.javafx.assets.JavaFxJsonAssetLoader;
import technology.sola.engine.platform.javafx.assets.graphics.JavaFxSolaImageAssetLoader;
import technology.sola.engine.platform.javafx.core.JavaFxGameLoop;
import technology.sola.engine.platform.javafx.core.JavaFxRenderer;
import technology.sola.logging.SolaLogger;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * JavaFxSolaPlatform is a {@link SolaPlatform} implementation for running a {@link technology.sola.engine.core.Sola} in
 * a JavaFX powered window.
 */
@NullMarked
public class JavaFxSolaPlatform extends SolaPlatform {
  private static final SolaLogger LOGGER = SolaLogger.of(JavaFxSolaPlatform.class);
  private final boolean useSoftwareRendering;
  private final boolean useImageSmoothing;
  @Nullable
  private final Double initialWindowWidth;
  @Nullable
  private final Double initialWindowHeight;
  private Canvas canvas;
  private GraphicsContext graphicsContext;
  private WritableImage writableImage;
  private Affine originalTransform;

  /**
   * Creates a JavaFxSolaPlatform instance with default {@link JavaFxSolaPlatformConfig}.
   */
  public JavaFxSolaPlatform() {
    this(new JavaFxSolaPlatformConfig());
  }

  /**
   * Creates a SwingSolaPlatform instance with desired configuration.
   *
   * @param platformConfig the {@link JavaFxSolaPlatformConfig}
   */
  public JavaFxSolaPlatform(JavaFxSolaPlatformConfig platformConfig) {
    this.useSoftwareRendering = platformConfig.useSoftwareRendering();
    this.useImageSmoothing = platformConfig.useImageSmoothing();
    this.initialWindowWidth = platformConfig.initialWindowWidth();
    this.initialWindowHeight = platformConfig.initialWindowHeight();

    socketClient = new JavaSocketClient();
    restClient = new JavaRestClient();
  }

  @Override
  public SolaPlatformIdentifier getIdentifier() {
    return SolaPlatformIdentifier.JAVA_FX;
  }

  @Override
  public void onKeyPressed(Consumer<KeyEvent> keyEventConsumer) {
    canvas.addEventHandler(
      javafx.scene.input.KeyEvent.KEY_PRESSED, keyEvent -> keyEventConsumer.accept(fxToSola(keyEvent))
    );
  }

  @Override
  public void onKeyReleased(Consumer<KeyEvent> keyEventConsumer) {
    canvas.addEventHandler(
      javafx.scene.input.KeyEvent.KEY_RELEASED, keyEvent -> keyEventConsumer.accept(fxToSola(keyEvent))
    );
  }

  @Override
  public void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer) {
    canvas.addEventHandler(
      javafx.scene.input.MouseEvent.MOUSE_MOVED, mouseEvent -> mouseEventConsumer.accept(fxToSola(mouseEvent))
    );
    canvas.addEventHandler(
      javafx.scene.input.MouseEvent.MOUSE_DRAGGED, mouseEvent -> mouseEventConsumer.accept(fxToSola(mouseEvent))
    );
  }

  @Override
  public void onMousePressed(Consumer<MouseEvent> mouseEventConsumer) {
    canvas.addEventHandler(
      javafx.scene.input.MouseEvent.MOUSE_PRESSED, mouseEvent -> mouseEventConsumer.accept(fxToSola(mouseEvent))
    );
  }

  @Override
  public void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer) {
    canvas.addEventHandler(
      javafx.scene.input.MouseEvent.MOUSE_RELEASED, mouseEvent -> mouseEventConsumer.accept(fxToSola(mouseEvent))
    );
  }

  @Override
  public void onMouseWheel(Consumer<MouseWheelEvent> mouseWheelEventConsumer) {
    canvas.addEventHandler(javafx.scene.input.ScrollEvent.SCROLL, scrollEvent -> {
      boolean isUp = scrollEvent.getDeltaY() > 0;
      boolean isDown = scrollEvent.getDeltaY() < 0;
      boolean isLeft = scrollEvent.getDeltaX() < 0;
      boolean isRight = scrollEvent.getDeltaX() > 0;

      mouseWheelEventConsumer.accept(new MouseWheelEvent(isUp, isDown, isLeft, isRight));
    });
  }

  @Override
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {
    SolaJavaFx.startOnApplicationThread(() -> {
      final Stage stage = new Stage();
      final Group root = new Group();
      int rendererWidth = solaConfiguration.rendererWidth();
      int rendererHeight = solaConfiguration.rendererHeight();
      final Scene scene = new Scene(root, rendererWidth, rendererHeight);

      setApplicationIcon(stage);

      this.canvas = new Canvas(rendererWidth, rendererHeight);
      root.getChildren().add(canvas);

      canvas.widthProperty().bind(scene.widthProperty());
      canvas.heightProperty().bind(scene.heightProperty());
      canvas.widthProperty().addListener((obs, oldVal, newVal) -> viewport.resize(newVal.intValue(), (int) canvas.getHeight()));
      canvas.heightProperty().addListener((obs, oldVal, newVal) -> viewport.resize((int) canvas.getWidth(), newVal.intValue()));

      graphicsContext = canvas.getGraphicsContext2D();
      graphicsContext.setImageSmoothing(useImageSmoothing);
      originalTransform = graphicsContext.getTransform();
      writableImage = new WritableImage(rendererWidth, rendererHeight);

      solaEventHub.add(GameLoopEvent.class, event -> {
        if (event.state() == GameLoopState.STOPPED) {
          if (socketClient.isConnected()) {
            socketClient.disconnect();
          }
          stage.close();
        }
      });

      stage.setOnShown(event -> canvas.requestFocus());
      stage.iconifiedProperty().addListener((observable, oldValue, isMinimized) -> {
        if (isMinimized) {
          solaEventHub.emit(new GameLoopEvent(GameLoopState.PAUSE));
        } else {
          solaEventHub.emit(new GameLoopEvent(GameLoopState.RESUME));
        }
      });
      stage.setOnCloseRequest(event -> solaEventHub.emit(new GameLoopEvent(GameLoopState.STOP)));
      stage.setTitle(solaConfiguration.title());
      stage.setScene(scene);
      if (initialWindowWidth != null) {
        stage.setWidth(initialWindowWidth);
      }
      if (initialWindowHeight != null) {
        stage.setHeight(initialWindowHeight);
      }
      stage.show();

      solaPlatformInitialization.finish();
    });
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    if (!useSoftwareRendering) {
      graphicsContext.setTransform(originalTransform);

      AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();

      graphicsContext.translate(aspectRatioSizing.x(), aspectRatioSizing.y());
      graphicsContext.scale(aspectRatioSizing.width() / (double) renderer.getWidth(), aspectRatioSizing.height() / (double) renderer.getHeight());
    }
  }

  @Override
  protected void onRender(Renderer renderer) {
    if (useSoftwareRendering) {
      int[] pixels = ((SoftwareRenderer) renderer).getPixels();

      writableImage.getPixelWriter().setPixels(
        0, 0, renderer.getWidth(), renderer.getHeight(),
        PixelFormat.getIntArgbInstance(), pixels, 0, renderer.getWidth()
      );

      AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();

      graphicsContext.drawImage(writableImage, aspectRatioSizing.x(), aspectRatioSizing.y(), aspectRatioSizing.width(), aspectRatioSizing.height());
    }
  }

  @Override
  protected void populateAssetLoaderProvider(AssetLoaderProvider assetLoaderProvider) {
    AssetLoader<SolaImage> solaImageAssetLoader = new JavaFxSolaImageAssetLoader();
    AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader = new JavaFxJsonAssetLoader();

    assetLoaderProvider.add(solaImageAssetLoader);
    assetLoaderProvider.add(jsonElementAssetAssetLoader);
    assetLoaderProvider.add(new JavaFxAudioClipAssetLoader());

    assetLoaderProvider.add(new FontAssetLoader(
      jsonElementAssetAssetLoader, solaImageAssetLoader
    ));
    assetLoaderProvider.add(new SpriteSheetAssetLoader(
      jsonElementAssetAssetLoader, solaImageAssetLoader
    ));
    assetLoaderProvider.add(new ControlsConfigAssetLoader(
      jsonElementAssetAssetLoader
    ));
  }

  @Override
  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    LOGGER.info("Using %s rendering", useSoftwareRendering ? "Software" : "GraphicsContext");

    return useSoftwareRendering
      ? super.buildRenderer(solaConfiguration)
      : new JavaFxRenderer(graphicsContext, solaConfiguration.rendererWidth(), solaConfiguration.rendererHeight());
  }

  @Override
  protected GameLoopProvider buildGameLoop() {
    return JavaFxGameLoop::new;
  }

  private KeyEvent fxToSola(javafx.scene.input.KeyEvent fxKeyEvent) {
    return new KeyEvent(fxKeyEvent.getCode().getCode());
  }

  private MouseEvent fxToSola(javafx.scene.input.MouseEvent fxMouseEvent) {
    MouseCoordinate adjusted = adjustMouseForViewport((int) fxMouseEvent.getX(), (int) fxMouseEvent.getY());

    return new MouseEvent(fxMouseEvent.getButton().ordinal(), adjusted.x(), adjusted.y());
  }

  private void setApplicationIcon(Stage stage) {
    try {
      var url = JavaFxPathUtils.asUrl("assets/icon.jpg");

      if (url == null) {
        url = JavaFxPathUtils.asUrl("assets/icon.png");
      }

      if (url == null) {
        LOGGER.warning("Icon not found");
      } else {
        stage.getIcons().add(new Image(url.openStream()));
      }
    } catch (IOException ex) {
      LOGGER.error("Failed to load icon", ex);
    }
  }
}
