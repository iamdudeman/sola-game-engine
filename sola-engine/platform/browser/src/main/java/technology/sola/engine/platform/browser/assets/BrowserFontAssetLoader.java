package technology.sola.engine.platform.browser.assets;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.font.FontInfo;
import technology.sola.engine.assets.graphics.font.mapper.FontInfoJsonMapper;
import technology.sola.engine.platform.browser.javascript.JsJsonUtils;
import technology.sola.json.SolaJson;

/**
 * A browser implementation of the {@link Font} {@link AssetLoader}.
 */
public class BrowserFontAssetLoader extends AssetLoader<Font> {
  private final AssetLoader<SolaImage> solaImageAssetLoader;

  /**
   * Creates an instance of this asset loader.
   *
   * @param solaImageAssetLoader the {@link SolaImage} {@link AssetLoader}
   */
  public BrowserFontAssetLoader(AssetLoader<SolaImage> solaImageAssetLoader) {
    this.solaImageAssetLoader = solaImageAssetLoader;
  }

  @Override
  public Class<Font> getAssetClass() {
    return Font.class;
  }

  @Override
  protected AssetHandle<Font> loadAsset(String path) {
    AssetHandle<Font> fontAssetHandle = new AssetHandle<>();

    JsJsonUtils.loadJson(path, jsonString -> {
      SolaJson solaJson = new SolaJson();
      FontInfo fontInfo = solaJson.parse(jsonString, new FontInfoJsonMapper());

      solaImageAssetLoader.getNewAsset(
          fontInfo.fontGlyphFile(),
          path.substring(0, path.lastIndexOf("/")) + "/" + fontInfo.fontGlyphFile()
        )
        .executeWhenLoaded(solaImage -> fontAssetHandle.setAsset(new Font(solaImage, fontInfo)));
    });

    return fontAssetHandle;
  }
}
