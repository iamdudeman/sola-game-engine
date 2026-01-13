package technology.sola.engine.scene;

import technology.sola.ecs.Component;
import technology.sola.engine.assets.scene.SceneAssetLoader;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.SolaPhysics;
import technology.sola.json.mapper.JsonMapper;

public class SceneConfiguration {
  private final SceneAssetLoader sceneAssetLoader;
  private final SceneDependencyModule sceneDependencyModule = new SceneDependencyModule();

  public SceneConfiguration(SceneAssetLoader sceneAssetLoader) {
    this.sceneAssetLoader = sceneAssetLoader;
  }

  public void bind(SceneDependencyBinding<?> binding) {
    sceneDependencyModule.bind(binding);
  }

  public void addComponentMapper(JsonMapper<? extends Component> mapper) {
//    sceneAssetLoader.addComponentMapper(mapper);
  }

  private class SceneSola extends Sola {
    private SolaGraphics solaGraphics;
    private SolaPhysics solaPhysics;

    /**
     * Creates a Sola instance with desired {@link SolaConfiguration}.
     *
     * @param configuration the configuration for the Sola
     */
    protected SceneSola(SolaConfiguration configuration) {
      super(configuration);
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onRender(Renderer renderer) {

    }
  }
}
