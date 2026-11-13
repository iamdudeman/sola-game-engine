package technology.sola.engine.platform.automation.asset;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.platform.automation.AutomationPathUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * An Automation implementation of the {@link SolaImage} {@link AssetLoader}.
 */
@NullMarked
public class AutomationSolaImageAssetLoader extends AssetLoader<SolaImage> {
  @Override
  public Class<SolaImage> getAssetClass() {
    return SolaImage.class;
  }

  @Override
  protected AssetHandle<SolaImage> loadAsset(String path) {
    try {
      BufferedImage bufferedImage = ImageIO.read(AutomationPathUtils.asUrl(path));
      SolaImage solaImage = new SolaImage(bufferedImage.getWidth(), bufferedImage.getHeight());

      bufferedImage.getRGB(0, 0, solaImage.getWidth(), solaImage.getHeight(), solaImage.getPixels(), 0, solaImage.getWidth());

      return new AssetHandle<>(solaImage);
    } catch (IOException ex) {
      throw new FailedSolaImageLoadException(path);
    }
  }
}
