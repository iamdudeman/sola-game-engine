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
import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.graphics.Renderer;

import java.util.function.Consumer;

public class SolaJavaFxPlatform extends AbstractSolaPlatform {
  private String title;
  private final int rendererWidth;
  private final int rendererHeight;
  private Consumer<int[]> pixelArrayConsumer = pixels -> {};

  public SolaJavaFxPlatform(String title, int rendererWidth, int rendererHeight) {
    this.title = title;
    this.rendererWidth = rendererWidth;
    this.rendererHeight = rendererHeight;
  }

  @Override
  public void init(AssetLoader assetLoader) {
    Platform.startup(() -> {});

    assetLoader.addAssetMapper(new SolaImageAssetMapper());
  }

  @Override
  public void start() {
    Platform.runLater(() -> {
      final Stage stage = new Stage();
      final Group root = new Group();
      final Scene scene = new Scene(root);
      Canvas canvas = new Canvas(rendererWidth, rendererHeight);

      root.getChildren().add(canvas);

      GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
      WritableImage writableImage = new WritableImage(rendererWidth, rendererHeight);
      pixelArrayConsumer = pixels -> {
        writableImage.getPixelWriter().setPixels(
          0, 0, rendererWidth, rendererHeight,
          PixelFormat.getIntArgbInstance(), pixels, 0, rendererWidth
        );
        graphicsContext.drawImage(writableImage, 0, 0, rendererWidth, rendererHeight);
      };

      stage.setOnCloseRequest(event -> System.exit(0)); // TODO somehow call AbstractSola#stop instead
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
