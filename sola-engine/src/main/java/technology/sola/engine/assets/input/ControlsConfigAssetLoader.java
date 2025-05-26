package technology.sola.engine.assets.input;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;

/**
 * ControlsConfigAssetLoader is an {@link AssetLoader} implementation for {@link ControlsConfig}s.
 */
@NullMarked
public class ControlsConfigAssetLoader extends AssetLoader<ControlsConfig> {
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;

  /**
   * Creates an instance of this asset loader.
   *
   * @param jsonElementAssetAssetLoader the {@link JsonElementAsset} {@link AssetLoader} to use internally
   */
  public ControlsConfigAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
  }

  @Override
  public Class<ControlsConfig> getAssetClass() {
    return ControlsConfig.class;
  }

  @Override
  protected AssetHandle<ControlsConfig> loadAsset(String path) {
    AssetHandle<ControlsConfig> inputConfigAssetHandle = new AssetHandle<>();

    jsonElementAssetAssetLoader.getNewAsset(path, path)
      .executeWhenLoaded(jsonElementAsset -> {
        var mapper = new ControlsConfigJsonMapper();

        inputConfigAssetHandle.setAsset(
          mapper.toObject(jsonElementAsset.asObject())
        );
      });


    return inputConfigAssetHandle;
  }
}
