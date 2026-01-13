package technology.sola.engine.assets.scene;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.ecs.io.json.JsonWorldIo;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.json.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public class SceneAssetLoader extends AssetLoader<Scene> {
//  private final List<JsonMapper<? extends Component>> jsonMapperList = new ArrayList<>();
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;
  private JsonWorldIo jsonWorldIo;

  public SceneAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
    jsonWorldIo = new JsonWorldIo(List.of());
  }

  public void configure(List<JsonMapper<? extends Component>> jsonMappers) {
    jsonWorldIo = new JsonWorldIo(jsonMappers);
  }

//  public String serialize(World world) {
//    return jsonWorldIo.stringify(world);
//  }
//
//  public World parse(String json) {
//    return jsonWorldIo.parse(json);
//  }

  @Override
  public Class<Scene> getAssetClass() {
    return Scene.class;
  }

  @Override
  protected AssetHandle<Scene> loadAsset(String path) {
    AssetHandle<Scene> assetHandle = new AssetHandle<>();

    jsonElementAssetAssetLoader.getNewAsset(path, path)
      .executeWhenLoaded(jsonElementAsset -> assetHandle.setAsset(
        new Scene(jsonWorldIo.parse(jsonElementAsset.toString()))
      ));

    return assetHandle;
  }
}
