package technology.sola.engine.platform.javafx;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.KeyEvent;

import java.util.function.Consumer;

public class JavaFxSolaPlatform extends AbstractSolaPlatform {
  private String title;
  private Consumer<int[]> pixelArrayConsumer = pixels -> { };

  public JavaFxSolaPlatform(String title) {
    this.title = title;
  }

  @Override
  public void init() {
    Platform.startup(() -> { });

    assetLoader.addAssetMapper(new SolaImageAssetMapper());
  }

  @Override
  public void start() {
    Platform.runLater(() -> {
      final Stage stage = new Stage();
      final Group root = new Group();
      final Scene scene = new Scene(root);
      int rendererWidth = abstractSola.getRendererWidth();
      int rendererHeight = abstractSola.getRendererHeight();
      Canvas canvas = new Canvas(rendererWidth, rendererHeight);

      root.getChildren().add(canvas);

      canvas.setOnKeyPressed(keyEvent -> onKeyPressed(new KeyEvent(keyEvent.getCode().getCode())));
      canvas.setOnKeyReleased(keyEvent -> onKeyReleased(new KeyEvent(keyEvent.getCode().getCode())));

      GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
      WritableImage writableImage = new WritableImage(rendererWidth, rendererHeight);
      pixelArrayConsumer = pixels -> {
        writableImage.getPixelWriter().setPixels(
          0, 0, rendererWidth, rendererHeight,
          PixelFormat.getIntArgbInstance(), pixels, 0, rendererWidth
        );
        graphicsContext.drawImage(writableImage, 0, 0, rendererWidth, rendererHeight);
      };

      stage.setOnShown(event -> canvas.requestFocus());
      stage.setOnCloseRequest(event -> eventHub.emit(GameLoopEvent.STOP));
      stage.setTitle(title);
      stage.setScene(scene);
      stage.show();
    });
  }

  @Override
  public void render(Renderer renderer) {
    renderer.render(pixelArrayConsumer);
  }
}