package technology.sola.engine.platform.swing.assets;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.platform.swing.assets.exception.FailedSolaImageLoadException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SolaImageAssetPool extends AssetPool<SolaImage> {
  @Override
  public Class<SolaImage> getAssetClass() {
    return SolaImage.class;
  }

  @Override
  protected SolaImage loadAsset(String path) {
    try {
      File file = new File(path);
      BufferedImage bufferedImage = ImageIO.read(file);
      SolaImage solaImage = new SolaImage(bufferedImage.getWidth(), bufferedImage.getHeight());

      bufferedImage.getRGB(0, 0, solaImage.getWidth(), solaImage.getHeight(), solaImage.getPixels(), 0, solaImage.getWidth());

      return solaImage;
    } catch (IOException ex) {
      throw new FailedSolaImageLoadException(path);
    }
  }
}