package technology.sola.engine.assets.list;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.json.mapper.JsonMapper;

/**
 * AssetListAssetLoader is an {@link AssetLoader} implementation for {@link AssetList}s.
 */
@NullMarked
public class AssetListAssetLoader extends AssetLoader<AssetList> {
  private final JsonMapper<AssetList> assetListJsonMapper = new AssetListJsonMapper();
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;

  /**
   * Creates an instance of this asset loader.
   *
   * @param jsonElementAssetAssetLoader the {@link AssetLoader} for {@link JsonElementAsset}s used internally
   */
  public AssetListAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
  }

  @Override
  public Class<AssetList> getAssetClass() {
    return AssetList.class;
  }

  @Override
  protected AssetHandle<AssetList> loadAsset(String path) {
    AssetHandle<AssetList> assetHandle = new AssetHandle<>();

    jsonElementAssetAssetLoader.getNewAsset(path, path)
      .executeWhenLoaded(jsonElementAsset -> {
        var assetList = jsonElementAsset.asObject();

        assetHandle.setAsset(assetListJsonMapper.toObject(assetList));
      });

    return assetHandle;
  }
}
