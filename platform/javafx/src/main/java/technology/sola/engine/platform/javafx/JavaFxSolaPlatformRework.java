package technology.sola.engine.platform.javafx;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.core.rework.SolaConfiguration;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.platform.javafx.core.JavaFxGameLoop;

import java.util.function.Consumer;

public class JavaFxSolaPlatformRework extends AbstractSolaPlatformRework {
  private Canvas canvas;
  private Consumer<int[]> pixelArrayConsumer = pixels -> { };

  @Override
  public void onKeyPressed(Consumer<KeyEvent> keyEventConsumer) {
    canvas.setOnKeyPressed(keyEvent -> keyEventConsumer.accept(new KeyEvent(keyEvent.getCode().getCode())));
  }

  @Override
  public void onKeyReleased(Consumer<KeyEvent> keyEventConsumer) {
    canvas.setOnKeyReleased(keyEvent -> keyEventConsumer.accept(new KeyEvent(keyEvent.getCode().getCode())));
  }

  @Override
  public void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer) {
    canvas.setOnMouseMoved(mouseEvent -> mouseEventConsumer.accept(
      new MouseEvent(mouseEvent.getButton().ordinal(), (int)mouseEvent.getX(), (int)mouseEvent.getY()))
    );
  }

  @Override
  public void onMousePressed(Consumer<MouseEvent> mouseEventConsumer) {
    canvas.setOnMousePressed(mouseEvent -> mouseEventConsumer.accept(
      new MouseEvent(mouseEvent.getButton().ordinal(), (int)mouseEvent.getX(), (int)mouseEvent.getY()))
    );

  }

  @Override
  public void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer) {
    canvas.setOnMouseReleased(mouseEvent -> mouseEventConsumer.accept(
      new MouseEvent(mouseEvent.getButton().ordinal(), (int)mouseEvent.getX(), (int)mouseEvent.getY()))
    );
  }

  @Override
  protected void initializePlatform(AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration, Runnable initCompleteCallback) {
    // TODO AssetPool stuff (probably will need new file system work)

    Platform.startup(() -> {
      final Stage stage = new Stage();
      final Group root = new Group();
      int rendererWidth = solaConfiguration.getCanvasWidth();
      int rendererHeight = solaConfiguration.getCanvasHeight();
      final Scene scene = new Scene(root, rendererWidth, rendererHeight);

      this.canvas = new Canvas(rendererWidth, rendererHeight);

      root.getChildren().add(canvas);

      canvas.widthProperty().bind(scene.widthProperty());
      canvas.heightProperty().bind(scene.heightProperty());
      canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
        viewport.resize(newVal.intValue(), (int) canvas.getHeight());
      });
      canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
        viewport.resize((int) canvas.getWidth(), newVal.intValue());
      });

      GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
      WritableImage writableImage = new WritableImage(rendererWidth, rendererHeight);
      pixelArrayConsumer = pixels -> {
        writableImage.getPixelWriter().setPixels(
          0, 0, rendererWidth, rendererHeight,
          PixelFormat.getIntArgbInstance(), pixels, 0, rendererWidth
        );

        AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.drawImage(writableImage, aspectRatioSizing.getX(), aspectRatioSizing.getY(), aspectRatioSizing.getWidth(), aspectRatioSizing.getHeight());
      };

      stage.setOnShown(event -> canvas.requestFocus());
      stage.setOnCloseRequest(event -> solaEventHub.emit(GameLoopEvent.STOP));
      stage.setTitle(solaConfiguration.getSolaTitle());
      stage.setScene(scene);
      stage.show();

      initCompleteCallback.run();
    });
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    // Nothing to do here
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.render(pixelArrayConsumer);
  }

  @Override
  protected GameLoopProvider buildGameLoop() {
    return JavaFxGameLoop::new;
  }
}
