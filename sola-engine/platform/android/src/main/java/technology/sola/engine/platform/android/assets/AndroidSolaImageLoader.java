package technology.sola.engine.platform.android.assets;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;

import java.io.IOException;
import java.io.InputStream;

@NullMarked
public class AndroidSolaImageLoader extends AssetLoader<SolaImage> {
  private final AssetManager assetManager;

  public AndroidSolaImageLoader(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  @Override
  public Class<SolaImage> getAssetClass() {
    return SolaImage.class;
  }

  @Override
  protected AssetHandle<SolaImage> loadAsset(String path) {
    AssetHandle<SolaImage> solaImageAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      try (InputStream inputStream = assetManager.open(AndroidAssetUtils.sanitizeAssetPath(path))) {
        var bitMap = BitmapFactory.decodeStream(inputStream);
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        SolaImage solaImage = new SolaImage(width, height);

        bitMap.getPixels(
          solaImage.getPixels(), 0, width, 0, 0, width, height
        );

        solaImageAssetHandle.setAsset(solaImage);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }).start();

    return solaImageAssetHandle;
  }
}
