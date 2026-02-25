package technology.sola.engine.assets.scene;

import technology.sola.ecs.Component;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.json.mapper.JsonMapper;

import java.util.List;

public class SceneAssetLoaderConfigurator {
  public static void configure(AssetLoaderProvider assetLoaderProvider) {
    configure(assetLoaderProvider, getDefaultJsonMappers());
  }

  public static void configure(AssetLoaderProvider assetLoaderProvider, List<JsonMapper<? extends Component>> jsonMappers) {
    configure((SceneAssetLoader) assetLoaderProvider.get(Scene.class), jsonMappers);
  }

  public static void configure(SceneAssetLoader sceneAssetLoader) {
    configure(sceneAssetLoader, getDefaultJsonMappers());
  }

  public static void configure(SceneAssetLoader sceneAssetLoader, List<JsonMapper<? extends Component>> jsonMappers) {
    sceneAssetLoader.configure(jsonMappers);
  }

  public static List<JsonMapper<? extends Component>> getDefaultJsonMappers() {
    return List.of(
      new TransformComponent.Mapper(),
      new CameraComponent.Mapper(),
      new CircleRendererComponent.Mapper()
    );
  }

  private SceneAssetLoaderConfigurator() {
  }
}
