package technology.sola.engine.platform.browser.assets.graphics;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.platform.browser.javascript.JsImageUtils;

/**
 * A browser implementation of the {@link SolaImage} {@link AssetLoader}.
 */
@NullMarked
public class BrowserSolaImageAssetLoader extends AssetLoader<SolaImage> {
  @Override
  public Class<SolaImage> getAssetClass() {
    return SolaImage.class;
  }

  @Override
  protected AssetHandle<SolaImage> loadAsset(String path) {
    AssetHandle<SolaImage> solaImageAssetHandle = new AssetHandle<>();

    JsImageUtils.loadImage(path, new ImageLoadCallbackImpl(solaImageAssetHandle));

    return solaImageAssetHandle;
  }

  private record ImageLoadCallbackImpl(
    AssetHandle<SolaImage> solaImageAssetHandle
  ) implements JsImageUtils.ImageLoadCallback {
    @Override
    public void call(int width, int height, int[] uInt8Pixels) {
      int[] pixels = new int[width * height];
      int index = 0;

      for (int i = 0; i < width * height * 4; i += 4) {
        Color color = new Color(uInt8Pixels[i + 3], uInt8Pixels[i], uInt8Pixels[i + 1], uInt8Pixels[i + 2]);

        pixels[index] = color.hexInt();
        index++;
      }

      solaImageAssetHandle.setAsset(new SolaImage(width, height, pixels));
    }
  }
}
