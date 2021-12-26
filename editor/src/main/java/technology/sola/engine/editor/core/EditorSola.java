package technology.sola.engine.editor.core;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.graphics.SolaGraphics;
import technology.sola.engine.core.physics.SolaPhysics;
import technology.sola.engine.ecs.EcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Renderer;

import java.util.ArrayList;
import java.util.List;

public class EditorSola extends Sola {
  private SolaGraphics solaGraphics;
  private final List<EcsSystem> previouslyActiveSystems = new ArrayList<>();
  private SolaConfiguration solaConfiguration;
  private String[] layers = new String[0];

  public EditorSola(SolaConfiguration solaConfiguration) {
    this.solaConfiguration = solaConfiguration;
  }

  public void setWorld(World world) {
    ecsSystemContainer.setWorld(world);
  }

  public void setSolaConfiguration(SolaConfiguration solaConfiguration) {
    this.solaConfiguration = solaConfiguration;
  }

  public void setLayers(String[] layers) {
    this.layers = layers;
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

  public void stop() {
    eventHub.emit(GameLoopEvent.STOP);
  }

  @Override
  protected SolaConfiguration getConfiguration() {
    return solaConfiguration;
  }

  @Override
  protected void onInit() {
    // TODO get assets based on project structure (might need this for all Sola [think VirtualFileSystem of sorts maybe])
    SolaPhysics.use(eventHub, ecsSystemContainer);
    solaGraphics = SolaGraphics.use(ecsSystemContainer, platform.getRenderer(), assetPoolProvider);

    stopPreview();

    platform.getRenderer().createLayers(layers);
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();
  }
}
