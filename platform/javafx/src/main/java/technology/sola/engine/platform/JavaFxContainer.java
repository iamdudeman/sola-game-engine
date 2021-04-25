package technology.sola.engine.platform;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import technology.sola.engine.graphics.Renderer;

import java.nio.IntBuffer;

public class JavaFxContainer {
  private final String title;
  private final int width;
  private final int height;
  private final Renderer renderer;

  public JavaFxContainer(String title, int width, int height) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.renderer = new Renderer(width, height);
  }

  public Renderer getRenderer() {
    return renderer;
  }

  public void start(Stage stage) {
    final Group root = new Group();
    final Scene scene = new Scene(root);
    Canvas canvas = new Canvas(width, height);

    root.getChildren().add(canvas);

    render(canvas.getGraphicsContext2D());

    stage.setTitle(title);
    stage.setScene(scene);
    stage.show();
  }

  private void render(GraphicsContext graphicsContext) {
    IntBuffer buffer = IntBuffer.allocate(width * height);
    int[] bufferPixels = buffer.array();
    PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(width, height, buffer, PixelFormat.getIntArgbPreInstance());
    WritableImage writableImage = new WritableImage(pixelBuffer);

    renderer.render(pixels -> {
      System.arraycopy(pixels, 0, bufferPixels, 0, pixels.length);

      pixelBuffer.updateBuffer(b -> null);
      graphicsContext.drawImage(writableImage, 0, 0, width, height);
    });
  }
}
