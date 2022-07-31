package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.core.module.graphics.SolaGraphics;
import technology.sola.engine.core.module.physics.SolaPhysics;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;
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
    SolaPhysics.createInstance(eventHub, solaEcs);

    solaEcs.setWorld(buildWorld());

    solaGraphics = SolaGraphics.createInstance(solaEcs, platform.getRenderer(), assetPoolProvider);
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

    ParticleEmitterComponent fireParticleEmitterComponent = new ParticleEmitterComponent();

    fireParticleEmitterComponent.setParticleBlendMode(BlendMode.LINEAR_DODGE);
    fireParticleEmitterComponent.setParticleColor(new Color(230, 40, 45));
    fireParticleEmitterComponent.setParticleSizeBounds(6, 10);
    fireParticleEmitterComponent.setParticleLifeBounds(1, 1);
    fireParticleEmitterComponent.setParticleVelocityBounds(new Vector2D(-18f, -70f), new Vector2D(18f, 0));
    fireParticleEmitterComponent.setParticleEmissionDelay(0.1f);
    fireParticleEmitterComponent.setParticlesPerEmit(10);

    world.createEntity()
      .addComponent(fireParticleEmitterComponent)
      .addComponent(new TransformComponent(250, 500));

    ParticleEmitterComponent sparksParticleEmitterComponent = new ParticleEmitterComponent();

    sparksParticleEmitterComponent.setParticleBlendMode(BlendMode.DISSOLVE);
    sparksParticleEmitterComponent.setParticleColor(new Color(210, 80, 45));
    sparksParticleEmitterComponent.setParticleSizeBounds(6, 12);
    sparksParticleEmitterComponent.setParticleLifeBounds(1, 3);
    sparksParticleEmitterComponent.setParticleVelocityBounds(new Vector2D(-18f, -70f), new Vector2D(18f, 0));
    sparksParticleEmitterComponent.setParticleEmissionDelay(0.01f);
    sparksParticleEmitterComponent.setParticlesPerEmit(1);

    world.createEntity()
      .addComponent(sparksParticleEmitterComponent)
      .addComponent(new TransformComponent(400, 500));

    return world;
  }
}
