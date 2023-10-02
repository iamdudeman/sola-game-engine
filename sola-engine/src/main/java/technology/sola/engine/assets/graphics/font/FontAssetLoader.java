package technology.sola.engine.assets.graphics.font;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.mapper.FontInfoJsonMapper;
import technology.sola.engine.assets.json.JsonElementAsset;

/**
 * FontAssetLoader is an {@link AssetLoader} implementation for {@link Font}s.
 */
public class FontAssetLoader extends AssetLoader<Font> {
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;
  private final AssetLoader<SolaImage> solaImageAssetLoader;

  /**
   * Creates an instance of this asset loader.
   *
   * @param jsonElementAssetAssetLoader the {@link JsonElementAsset} {@link AssetLoader} to use internally
   * @param solaImageAssetLoader        the {@link SolaImage} {@link AssetLoader} to use internally
   */
  public FontAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader, AssetLoader<SolaImage> solaImageAssetLoader) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
    this.solaImageAssetLoader = solaImageAssetLoader;
  }

  @Override
  public Class<Font> getAssetClass() {
    return Font.class;
  }

  @Override
  protected AssetHandle<Font> loadAsset(String path) {
    AssetHandle<Font> fontAssetHandle = new AssetHandle<>();

    jsonElementAssetAssetLoader.getNewAsset(path, path)
      .executeWhenLoaded(jsonElementAsset -> {
        FontInfo fontInfo = new FontInfoJsonMapper().toObject(jsonElementAsset.jsonElement().asObject());

        solaImageAssetLoader.getNewAsset(
            fontInfo.fontGlyphFile(),
            path.substring(0, path.lastIndexOf("/")) + "/" + fontInfo.fontGlyphFile()
          )
          .executeWhenLoaded(solaImage -> fontAssetHandle.setAsset(new Font(solaImage, fontInfo)));
      });

    return fontAssetHandle;
  }
}
