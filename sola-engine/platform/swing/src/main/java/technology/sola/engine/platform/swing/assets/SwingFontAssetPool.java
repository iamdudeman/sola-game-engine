package technology.sola.engine.platform.swing.assets;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.font.FontInfo;
import technology.sola.engine.assets.graphics.font.mapper.FontInfoJsonMapper;
import technology.sola.engine.platform.swing.assets.exception.FailedFontLoadException;
import technology.sola.json.SolaJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SwingFontAssetPool extends AssetPool<Font> {
  private final AssetPool<SolaImage> solaImageAssetPool;

  public SwingFontAssetPool(AssetPool<SolaImage> solaImageAssetPool) {
    this.solaImageAssetPool = solaImageAssetPool;
  }

  @Override
  public Class<Font> getAssetClass() {
    return Font.class;
  }

  @Override
  protected AssetHandle<Font> loadAsset(String path) {
    AssetHandle<Font> fontAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      File file = new File(path);
      SolaJson solaJson = new SolaJson();

      try {
        FontInfo fontInfo = solaJson.parse(Files.readString(file.toPath()), new FontInfoJsonMapper());

        solaImageAssetPool.getNewAsset(
          fontInfo.fontGlyphFile(),
          path.replace(file.getName(), "") + fontInfo.fontGlyphFile()
        ).executeWhenLoaded(solaImage -> fontAssetHandle.setAsset(new Font(solaImage, fontInfo)));
      } catch (IOException ex) {
        throw new FailedFontLoadException(path);
      }

    }).start();

    return fontAssetHandle;
  }
}
