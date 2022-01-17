package technology.sola.engine.platform.browser.assets;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.platform.browser.javascript.JsImageUtils;

public class SolaImageAssetPool extends AssetPool<SolaImage> {
  @Override
  public Class<SolaImage> getAssetClass() {
    return SolaImage.class;
  }

  @Override
  protected SolaImage loadAsset(String path) {
    SolaImage solaImage = new SolaImage(0, 0);

    // TODO figure out a way to do this not async maybe
    JsImageUtils.loadImage(path, new ImageLoadCallbackImpl(solaImage));

    return solaImage;
  }

  private static class ImageLoadCallbackImpl implements JsImageUtils.ImageLoadCallback {
    private final SolaImage solaImage;

    public ImageLoadCallbackImpl(SolaImage solaImage) {
      this.solaImage = solaImage;
    }

    @Override
    public void call(int width, int height, int[] uInt8Pixels) {
      int[] pixels = new int[width * height];
      int index = 0;

      for (int i = 0; i < width * height * 4; i += 4) {
        Color color = new Color(uInt8Pixels[i + 3], uInt8Pixels[i], uInt8Pixels[i + 1], uInt8Pixels[i + 2]);

        pixels[index] = color.hexInt();
        index++;
      }

      solaImage.setPixels(width, height, pixels);
    }
  }
}
