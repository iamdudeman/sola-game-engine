package technology.sola.engine.platform.javafx;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.graphics.Renderer;

import java.nio.IntBuffer;

public abstract class SolaJavaFx extends AbstractSola {
  private String title;
  private GraphicsContext graphicsContext;

  protected SolaJavaFx(String title, int width, int height, int updatesPerSecond) {
    this.title = title;
    config(width, height, updatesPerSecond);
  }

  public void start(Stage stage) {
    final Group root = new Group();
    final Scene scene = new Scene(root);
    Canvas canvas = new Canvas(rendererWidth, rendererHeight);

    root.getChildren().add(canvas);

    graphicsContext = canvas.getGraphicsContext2D();

    stage.setOnCloseRequest(event -> stop());
    stage.setTitle(title);
    stage.setScene(scene);
    stage.show();

//    IntBuffer buffer = IntBuffer.allocate(rendererWidth * rendererHeight);
//    bufferPixels = buffer.array();
//    pixelBuffer = new PixelBuffer<>(rendererWidth, rendererHeight, buffer, PixelFormat.getIntArgbPreInstance());
    writableImage = new WritableImage(rendererWidth, rendererHeight);

    start();
  }

  int[] bufferPixels;
  PixelBuffer<IntBuffer> pixelBuffer;
  WritableImage writableImage;

  @Override
  protected void onRender(Renderer renderer) {
    render();
  }

  private void render() {
    renderer.render(pixels -> {
//      System.arraycopy(pixels, 0, bufferPixels, 0, pixels.length);
      writableImage.getPixelWriter().setPixels(0, 0, rendererWidth, rendererHeight, PixelFormat.getIntArgbInstance(), pixels, 0, rendererWidth);

//      pixelBuffer.updateBuffer(b -> null);
      graphicsContext.drawImage(writableImage, 0, 0, rendererWidth, rendererHeight);
    });
  }
}
