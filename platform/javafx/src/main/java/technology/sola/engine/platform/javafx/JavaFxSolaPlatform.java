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
import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.platform.javafx.assets.FontAssetPool;
import technology.sola.engine.platform.javafx.assets.SolaImageAssetPool;

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

    AssetPool<SolaImage> solaImageAssetPool = new SolaImageAssetPool();
    assetPoolProvider.addAssetPool(solaImageAssetPool);
    assetPoolProvider.addAssetPool(new FontAssetPool(solaImageAssetPool));
  }

  @Override
  public void start() {
    Platform.runLater(() -> {
      final Stage stage = new Stage();
      final Group root = new Group();
      int rendererWidth = abstractSola.getRendererWidth();
      int rendererHeight = abstractSola.getRendererHeight();
      final Scene scene = new Scene(root, rendererWidth, rendererHeight);
      Canvas canvas = new Canvas(rendererWidth, rendererHeight);

      root.getChildren().add(canvas);

      canvas.setOnKeyPressed(keyEvent -> onKeyPressed(new KeyEvent(keyEvent.getCode().getCode())));
      canvas.setOnKeyReleased(keyEvent -> onKeyReleased(new KeyEvent(keyEvent.getCode().getCode())));
      canvas.setOnMouseMoved(mouseEvent -> onMouseMoved(
        new MouseEvent(mouseEvent.getButton().ordinal(), (int)mouseEvent.getX(), (int)mouseEvent.getY()))
      );
      canvas.setOnMousePressed(mouseEvent -> onMousePressed(
        new MouseEvent(mouseEvent.getButton().ordinal(), (int)mouseEvent.getX(), (int)mouseEvent.getY()))
      );
      canvas.setOnMouseReleased(mouseEvent -> onMouseReleased(
        new MouseEvent(mouseEvent.getButton().ordinal(), (int)mouseEvent.getX(), (int)mouseEvent.getY()))
      );
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
