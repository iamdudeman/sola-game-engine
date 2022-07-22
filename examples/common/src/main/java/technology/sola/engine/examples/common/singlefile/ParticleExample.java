package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.core.graphics.SolaGraphics;
import technology.sola.engine.core.physics.SolaPhysics;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.physics.component.ParticleEmitterComponent;

public class ParticleExample extends Sola {
  private SolaGraphics solaGraphics;

  @Override
  protected SolaConfiguration getConfiguration() {
    return new SolaConfiguration("Particle Example", 800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    SolaPhysics.use(eventHub, solaEcs);

    solaEcs.setWorld(buildWorld());

    solaGraphics = SolaGraphics.use(solaEcs, platform.getRenderer(), assetPoolProvider);
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();
  }

  private World buildWorld() {
    World world = new World(10000);

    world.createEntity()
      .addComponent(new ParticleEmitterComponent())
      .addComponent(new TransformComponent(75, 500));

    return world;
  }
}
