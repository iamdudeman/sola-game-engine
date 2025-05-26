package technology.sola.engine.platform.javafx.assets.graphics;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.platform.javafx.assets.JavaFxPathUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * A JavaFX implementation of the {@link SolaImage} {@link AssetLoader}.
 */
@NullMarked
public class JavaFxSolaImageAssetLoader extends AssetLoader<SolaImage> {
  @Override
  public Class<SolaImage> getAssetClass() {
    return SolaImage.class;
  }

  @Override
  protected AssetHandle<SolaImage> loadAsset(String path) {
    AssetHandle<SolaImage> solaImageAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      try (InputStream inputStream = JavaFxPathUtils.asUrl(path).openStream()) {
        Image image = new Image(inputStream);
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        SolaImage solaImage = new SolaImage(width, height);

        image.getPixelReader()
          .getPixels(0, 0, width, height, PixelFormat.getIntArgbPreInstance(), solaImage.getPixels(), 0, width);

        solaImageAssetHandle.setAsset(solaImage);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }).start();

    return solaImageAssetHandle;
  }
}
