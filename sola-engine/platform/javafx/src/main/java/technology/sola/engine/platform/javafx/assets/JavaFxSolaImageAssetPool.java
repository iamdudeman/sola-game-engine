package technology.sola.engine.platform.javafx.assets;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.graphics.SolaImage;

import java.io.File;

public class JavaFxSolaImageAssetPool extends AssetPool<SolaImage> {
  @Override
  public Class<SolaImage> getAssetClass() {
    return SolaImage.class;
  }

  @Override
  protected AssetHandle<SolaImage> loadAsset(String path) {
    AssetHandle<SolaImage> solaImageAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      File file = new File(path);
      Image image = new Image(file.toURI().toString());
      int width = (int) image.getWidth();
      int height = (int) image.getHeight();

      SolaImage solaImage = new SolaImage(width, height);

      image.getPixelReader()
        .getPixels(0, 0, width, height, PixelFormat.getIntArgbPreInstance(), solaImage.getPixels(), 0, width);

      solaImageAssetHandle.setAsset(solaImage);
    }).start();

    return solaImageAssetHandle;
  }
}
