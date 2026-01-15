package technology.sola.engine.assets.scene;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.json.mapper.JsonMapper;

import java.util.List;

/**
 * SceneAssetLoader is an {@link AssetLoader} implementation for {@link Scene}s.
 */
@NullMarked
public class SceneAssetLoader extends AssetLoader<Scene> {
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;
  private final SceneJsonMapper sceneJsonMapper = new SceneJsonMapper();

  /**
   * Creates an instance of this asset loader.
   *
   * @param jsonElementAssetAssetLoader the {@link AssetLoader} for {@link JsonElementAsset}s used internally
   */
  public SceneAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
  }

  /**
   * Configures the {@link SceneAssetLoader} with the desired {@link Component} {@link JsonMapper}s.
   *
   * @param jsonMappers the component JSON mappers
   */
  public void configure(List<JsonMapper<? extends Component>> jsonMappers) {
    sceneJsonMapper.configure(jsonMappers);
  }

  @Override
  public Class<Scene> getAssetClass() {
    return Scene.class;
  }

  @Override
  protected AssetHandle<Scene> loadAsset(String path) {
    AssetHandle<Scene> assetHandle = new AssetHandle<>();

    jsonElementAssetAssetLoader.getNewAsset(path, path)
      .executeWhenLoaded(jsonElementAsset -> assetHandle.setAsset(
        sceneJsonMapper.toObject(jsonElementAsset.asObject())
      ));

    return assetHandle;
  }
}
