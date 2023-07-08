package technology.sola.engine.platform.swing.assets;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.font.FontInfo;
import technology.sola.engine.assets.graphics.font.mapper.FontInfoJsonMapper;
import technology.sola.engine.platform.swing.assets.exception.FailedFontLoadException;
import technology.sola.json.SolaJson;

import java.io.File;
import java.io.IOException;

public class SwingFontAssetLoader extends AssetLoader<Font> {
  private final AssetLoader<SolaImage> solaImageAssetLoader;

  public SwingFontAssetLoader(AssetLoader<SolaImage> solaImageAssetLoader) {
    this.solaImageAssetLoader = solaImageAssetLoader;
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
        FontInfo fontInfo = solaJson.parse(PathUtils.readContents(path), new FontInfoJsonMapper());

        solaImageAssetLoader.getNewAsset(
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
