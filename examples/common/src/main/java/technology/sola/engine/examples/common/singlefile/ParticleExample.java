package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.core.graphics.SolaGraphics;
import technology.sola.engine.core.physics.SolaPhysics;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.math.linear.Vector2D;

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
      .addComponent(new TransformComponent(100, 500));

    ParticleEmitterComponent particleEmitterComponent = new ParticleEmitterComponent();

    particleEmitterComponent.setParticleColor(new Color(230, 40, 45));
    particleEmitterComponent.setParticleSizeBounds(8f, 12f);
    particleEmitterComponent.setParticleLifeBounds(0.5f, 1f);
    particleEmitterComponent.setParticleVelocityBounds(new Vector2D(-10f, -300f), new Vector2D(10f, -50f));
    particleEmitterComponent.setParticleEmissionDelay(0.1f);
    particleEmitterComponent.setParticlesPerEmit(15);

    world.createEntity()
      .addComponent(particleEmitterComponent)
      .addComponent(new TransformComponent(350, 500));

    return world;
  }
}
