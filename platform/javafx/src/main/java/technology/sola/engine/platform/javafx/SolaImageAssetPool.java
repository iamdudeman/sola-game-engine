package technology.sola.engine.platform.javafx;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.graphics.SolaImage;

import java.io.File;

public class SolaImageAssetPool extends AssetPool<SolaImage> {
  @Override
  public Class<SolaImage> getAssetClass() {
    return SolaImage.class;
  }

  @Override
  protected SolaImage loadAsset(String path) {
    File file = new File(path);
    Image image = new Image(file.toURI().toString());
    int width = (int)image.getWidth();
    int height = (int)image.getHeight();

    SolaImage solaImage = new SolaImage(width, height);

    image.getPixelReader()
      .getPixels(0, 0, width, height, PixelFormat.getIntArgbPreInstance(), solaImage.getPixels(), 0, width);

    return solaImage;
  }
}
