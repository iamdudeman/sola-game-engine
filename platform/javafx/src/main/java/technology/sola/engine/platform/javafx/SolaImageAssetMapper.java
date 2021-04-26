package technology.sola.engine.platform.javafx;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import technology.sola.engine.assets.AssetMapper;
import technology.sola.engine.graphics.SolaImage;

import java.io.File;

public class SolaImageAssetMapper implements AssetMapper<SolaImage> {
  @Override
  public Class<SolaImage> getAssetType() {
    return SolaImage.class;
  }

  @Override
  public SolaImage mapToAssetType(File file) {
    Image image = new Image(file.toURI().toString());
    int width = (int)image.getWidth();
    int height = (int)image.getHeight();

    SolaImage solaImage = new SolaImage(width, height);

    image.getPixelReader()
      .getPixels(0, 0, width, height, PixelFormat.getIntArgbPreInstance(), solaImage.getPixels(), 0, width);

    return solaImage;
  }
}
