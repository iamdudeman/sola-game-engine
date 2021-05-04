package technology.sola.engine.platform.javafx;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import technology.sola.engine.core.AbstractSola;

public abstract class SolaJavaFx extends AbstractSola {
  private String title;
  private GraphicsContext graphicsContext;
  private WritableImage writableImage;

  protected SolaJavaFx(String title, int width, int height, int updatesPerSecond) {
    this.title = title;
    config(width, height, updatesPerSecond, true);
  }

  public void start(Stage stage) {
    final Group root = new Group();
    final Scene scene = new Scene(root);
    Canvas canvas = new Canvas(rendererWidth, rendererHeight);

    root.getChildren().add(canvas);

    graphicsContext = canvas.getGraphicsContext2D();

    stage.setOnCloseRequest(event -> stopGameLoop());
    stage.setTitle(title);
    stage.setScene(scene);
    stage.show();

    writableImage = new WritableImage(rendererWidth, rendererHeight);

    beginGameLoop();
  }

  @Override
  protected void onRender() {
    render();
  }

  private void render() {
    renderer.render(pixels -> {
      writableImage.getPixelWriter().setPixels(0, 0, rendererWidth, rendererHeight, PixelFormat.getIntArgbInstance(), pixels, 0, rendererWidth);

      graphicsContext.drawImage(writableImage, 0, 0, rendererWidth, rendererHeight);
    });
  }
}
