package technology.sola.engine.platform.javafx;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.FontAssetLoader;
import technology.sola.engine.assets.graphics.SpriteSheetAssetLoader;
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
import technology.sola.engine.platform.javafx.assets.audio.JavaFxAudioClipAssetLoader;
import technology.sola.engine.platform.javafx.assets.JavaFxJsonAssetLoader;
import technology.sola.engine.platform.javafx.assets.graphics.JavaFxSolaImageAssetLoader;
import technology.sola.engine.platform.javafx.core.JavaFxGameLoop;
import technology.sola.engine.platform.javafx.core.JavaFxRestClient;
import technology.sola.engine.platform.javafx.core.JavaFxSocketClient;

import java.util.function.Consumer;

/**
 * JavaFxSolaPlatform is a {@link SolaPlatform} implementation for running a {@link technology.sola.engine.core.Sola} in
 * a JavaFX powered window.
 */
public class JavaFxSolaPlatform extends SolaPlatform {
  private static boolean isPlatformStartupNeeded = true;
  private Canvas canvas;
  private GraphicsContext graphicsContext;
  private WritableImage writableImage;
  private Double windowWidth;
  private Double windowHeight;

  /**
   * Creates a JavaFxSolaPlatform instance.
   */
  public JavaFxSolaPlatform() {
    this(true);
  }

  /**
   * Creates a JavaFxSolaPlatform instance.
   *
   * @param isPlatformStartupNeeded whether JavaFX platform startup is still needed or not
   */
  public JavaFxSolaPlatform(boolean isPlatformStartupNeeded) {
    if (JavaFxSolaPlatform.isPlatformStartupNeeded) {
      JavaFxSolaPlatform.isPlatformStartupNeeded = isPlatformStartupNeeded;
    }
    socketClient = new JavaFxSocketClient();
    restClient = new JavaFxRestClient();
  }

  /**
   * Sets the initial window size when a {@link technology.sola.engine.core.Sola} is played.
   *
   * @param width  the width of the window
   * @param height the height of the window
   */
  public void setWindowSize(int width, int height) {
    windowWidth = (double) width;
    windowHeight = (double) height;
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
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {
    if (isPlatformStartupNeeded) {
      Platform.startup(() -> {
      });
      isPlatformStartupNeeded = false;
    }

    Platform.runLater(() -> {
      final Stage stage = new Stage();
      final Group root = new Group();
      int rendererWidth = solaConfiguration.rendererWidth();
      int rendererHeight = solaConfiguration.rendererHeight();
      final Scene scene = new Scene(root, rendererWidth, rendererHeight);

      this.canvas = new Canvas(rendererWidth, rendererHeight);
      root.getChildren().add(canvas);

      canvas.widthProperty().bind(scene.widthProperty());
      canvas.heightProperty().bind(scene.heightProperty());
      canvas.widthProperty().addListener((obs, oldVal, newVal) -> viewport.resize(newVal.intValue(), (int) canvas.getHeight()));
      canvas.heightProperty().addListener((obs, oldVal, newVal) -> viewport.resize((int) canvas.getWidth(), newVal.intValue()));

      graphicsContext = canvas.getGraphicsContext2D();
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
      if (windowWidth != null) {
        stage.setWidth(windowWidth);
      }
      if (windowHeight != null) {
        stage.setHeight(windowHeight);
      }
      stage.show();

      solaPlatformInitialization.finish();
    });
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    // Nothing to do here
  }

  @Override
  protected void onRender(Renderer renderer) {
    int[] pixels = ((SoftwareRenderer) renderer).getPixels();
    writableImage.getPixelWriter().setPixels(
      0, 0, renderer.getWidth(), renderer.getHeight(),
      PixelFormat.getIntArgbInstance(), pixels, 0, renderer.getWidth()
    );

    AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();
    graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    graphicsContext.drawImage(writableImage, aspectRatioSizing.x(), aspectRatioSizing.y(), aspectRatioSizing.width(), aspectRatioSizing.height());
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
}
