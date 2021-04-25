package technology.sola.engine.platform;

import technology.sola.engine.assets.AssetMapper;
import technology.sola.engine.graphics.SolaImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SolaImageAssetMapper implements AssetMapper<SolaImage> {
  @Override
  public Class<SolaImage> getAssetType() {
    return SolaImage.class;
  }

  @Override
  public SolaImage mapToAssetType(File file) {
    try {
      BufferedImage bufferedImage = ImageIO.read(file);

      SolaImage solaImage = new SolaImage(bufferedImage.getWidth(), bufferedImage.getHeight());
      bufferedImage.getRGB(0, 0, solaImage.getWidth(), solaImage.getHeight(), solaImage.getPixels(), 0, solaImage.getWidth());

      return solaImage;
    } catch (IOException ex) {
      // todo
      ex.printStackTrace();
    }

    return null;
  }
}
