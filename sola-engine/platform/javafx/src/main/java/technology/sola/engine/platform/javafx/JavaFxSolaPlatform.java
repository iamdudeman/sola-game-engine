package technology.sola.engine.platform.javafx;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopEventType;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.platform.javafx.assets.JavaFxAudiClipAssetPool;
import technology.sola.engine.platform.javafx.assets.JavaFxFontAssetPool;
import technology.sola.engine.platform.javafx.assets.JavaFxSolaImageAssetPool;
import technology.sola.engine.platform.javafx.assets.JavaFxSpriteSheetAssetPool;
import technology.sola.engine.platform.javafx.core.JavaFxGameLoop;

import java.util.function.Consumer;

public class JavaFxSolaPlatform extends SolaPlatform {
  private static boolean isPlatformStartupNeeded = true;
  private Canvas canvas;
  private GraphicsContext graphicsContext;
  private WritableImage writableImage;

  public JavaFxSolaPlatform() {
  }

  public JavaFxSolaPlatform(boolean isPlatformStartupNeeded) {
    if (JavaFxSolaPlatform.isPlatformStartupNeeded) {
      JavaFxSolaPlatform.isPlatformStartupNeeded = isPlatformStartupNeeded;
    }
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
      Platform.startup(() -> { });
      isPlatformStartupNeeded = false;
    }

    Platform.runLater(() -> {
      final Stage stage = new Stage();
      final Group root = new Group();
      int rendererWidth = solaConfiguration.canvasWidth();
      int rendererHeight = solaConfiguration.canvasHeight();
      final Scene scene = new Scene(root, rendererWidth, rendererHeight);

      this.canvas = new Canvas(rendererWidth, rendererHeight);
      root.getChildren().add(canvas);

      canvas.widthProperty().bind(scene.widthProperty());
      canvas.heightProperty().bind(scene.heightProperty());
      canvas.widthProperty().addListener((obs, oldVal, newVal) -> viewport.resize(newVal.intValue(), (int) canvas.getHeight()));
      canvas.heightProperty().addListener((obs, oldVal, newVal) -> viewport.resize((int) canvas.getWidth(), newVal.intValue()));

      graphicsContext = canvas.getGraphicsContext2D();
      writableImage = new WritableImage(rendererWidth, rendererHeight);

      solaEventHub.add(event -> {
        if (event.getMessage() == GameLoopEventType.STOP) {
          stage.close();
        }
      }, GameLoopEvent.class);

      stage.setOnShown(event -> canvas.requestFocus());
      stage.setOnCloseRequest(event -> solaEventHub.emit(GameLoopEvent.STOP));
      stage.setTitle(solaConfiguration.solaTitle());
      stage.setScene(scene);
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
  protected void populateAssetPoolProvider(AssetPoolProvider assetPoolProvider) {
    AssetPool<SolaImage> solaImageAssetPool = new JavaFxSolaImageAssetPool();
    assetPoolProvider.addAssetPool(solaImageAssetPool);
    assetPoolProvider.addAssetPool(new JavaFxFontAssetPool(solaImageAssetPool));
    assetPoolProvider.addAssetPool(new JavaFxSpriteSheetAssetPool(solaImageAssetPool));
    assetPoolProvider.addAssetPool(new JavaFxAudiClipAssetPool());
  }

  @Override
  protected GameLoopProvider buildGameLoop() {
    return JavaFxGameLoop::new;
  }

  private KeyEvent fxToSola(javafx.scene.input.KeyEvent fxKeyEvent) {
    return new KeyEvent(fxKeyEvent.getCode().getCode());
  }

  private MouseEvent fxToSola(javafx.scene.input.MouseEvent fxMouseEvent) {
    return new MouseEvent(fxMouseEvent.getButton().ordinal(), (int)fxMouseEvent.getX(), (int)fxMouseEvent.getY());
  }
}
