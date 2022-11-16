package technology.sola.engine.platform.javafx.assets;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;

import java.io.IOException;
import java.io.InputStream;

public class JavaFxSolaImageAssetLoader extends AssetLoader<SolaImage> {
  @Override
  public Class<SolaImage> getAssetClass() {
    return SolaImage.class;
  }

  @Override
  protected AssetHandle<SolaImage> loadAsset(String path) {
    AssetHandle<SolaImage> solaImageAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      try (InputStream inputStream = PathUtils.asUrl(path).openStream()) {
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
