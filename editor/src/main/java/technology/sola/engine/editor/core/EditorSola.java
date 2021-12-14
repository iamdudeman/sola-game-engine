package technology.sola.engine.editor.core;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.graphics.SolaGraphics;
import technology.sola.engine.core.physics.SolaPhysics;
import technology.sola.engine.ecs.EcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Renderer;

import java.util.ArrayList;
import java.util.List;

public class EditorSola extends Sola {
  private SolaGraphics solaGraphics;
  private final List<EcsSystem> previouslyActiveSystems = new ArrayList<>();

  public void setWorld(World world) {
    ecsSystemContainer.setWorld(world);
  }

  public void startPreview() {
    previouslyActiveSystems.forEach(activeSystem -> {
      ecsSystemContainer.get(activeSystem.getClass()).setActive(true);
    });
    previouslyActiveSystems.clear();
  }

  public void stopPreview() {
    ecsSystemContainer.activeSystemsIterator().forEachRemaining(activeSystem -> {
      previouslyActiveSystems.add(activeSystem);
      activeSystem.setActive(false);
    });
  }

  @Override
  protected SolaConfiguration buildConfiguration() {
    // TODO get this from project settings instead
    return new SolaConfiguration("Preview", 800, 600, 10, true);
  }

  @Override
  protected void onInit() {
    // TODO get assets based on project structure (might need this for all Sola [think VirtualFileSystem of sorts maybe])
    SolaPhysics.use(eventHub, ecsSystemContainer);
    solaGraphics = SolaGraphics.use(ecsSystemContainer, platform.getRenderer(), assetPoolProvider);

    stopPreview();
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();
  }
}
