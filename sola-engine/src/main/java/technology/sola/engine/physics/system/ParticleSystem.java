package technology.sola.engine.physics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;

public class ParticleSystem extends EcsSystem {
  @Override
  public void update(World world, float v) {
    world.createView().of(ParticleEmitterComponent.class, TransformComponent.class)
      .forEach(view -> {
        // todo
      });
  }
}
