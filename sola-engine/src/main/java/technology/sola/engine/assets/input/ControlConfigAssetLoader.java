package technology.sola.engine.assets.input;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;

/**
 * ControlConfigAssetLoader is an {@link AssetLoader} implementation for {@link ControlConfig}s.
 */
public class ControlConfigAssetLoader extends AssetLoader<ControlConfig> {
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;

  /**
   * Creates an instance of this asset loader.
   *
   * @param jsonElementAssetAssetLoader the {@link JsonElementAsset} {@link AssetLoader} to use internally
   */
  public ControlConfigAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
  }

  @Override
  public Class<ControlConfig> getAssetClass() {
    return ControlConfig.class;
  }

  @Override
  protected AssetHandle<ControlConfig> loadAsset(String path) {
    AssetHandle<ControlConfig> inputConfigAssetHandle = new AssetHandle<>();

    jsonElementAssetAssetLoader.getNewAsset(path, path)
      .executeWhenLoaded(jsonElementAsset -> {
        var mapper = new ControlConfigJsonMapper();

        inputConfigAssetHandle.setAsset(
          mapper.toObject(jsonElementAsset.asObject())
        );
      });


    return inputConfigAssetHandle;
  }
}
