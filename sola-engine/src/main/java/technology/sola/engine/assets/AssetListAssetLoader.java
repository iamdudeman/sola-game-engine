package technology.sola.engine.assets;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.json.JsonElementAsset;

import java.util.List;

@NullMarked
public class AssetListAssetLoader extends AssetLoader<AssetList> {
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;

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
        assetHandle.setAsset(
          // todo populate
          new AssetList(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()
          )
        );
      });

    return assetHandle;
  }
}
