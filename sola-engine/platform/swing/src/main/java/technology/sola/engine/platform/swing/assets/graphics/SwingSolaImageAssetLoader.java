package technology.sola.engine.platform.swing.assets.graphics;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.platform.swing.assets.SwingPathUtils;
import technology.sola.engine.platform.swing.assets.exception.FailedSolaImageLoadException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A Swing implementation of the {@link SolaImage} {@link AssetLoader}.
 */
public class SwingSolaImageAssetLoader extends AssetLoader<SolaImage> {
  @Override
  public Class<SolaImage> getAssetClass() {
    return SolaImage.class;
  }

  @Override
  protected AssetHandle<SolaImage> loadAsset(String path) {
    AssetHandle<SolaImage> solaImageAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      try {
        BufferedImage bufferedImage = ImageIO.read(SwingPathUtils.asUrl(path));
        SolaImage solaImage = new SolaImage(bufferedImage.getWidth(), bufferedImage.getHeight());

        bufferedImage.getRGB(0, 0, solaImage.getWidth(), solaImage.getHeight(), solaImage.getPixels(), 0, solaImage.getWidth());

        solaImageAssetHandle.setAsset(solaImage);
      } catch (IOException ex) {
        throw new FailedSolaImageLoadException(path);
      }
    }).start();

    return solaImageAssetHandle;
  }
}
