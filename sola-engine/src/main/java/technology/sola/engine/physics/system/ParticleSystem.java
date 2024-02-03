package technology.sola.engine.physics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;

public class ParticleSystem extends EcsSystem {
  @Override
  public void update(World world, float delta) {
    world.createView().of(ParticleEmitterComponent.class, TransformComponent.class)
      .getEntries()
      .forEach(view -> {
        // update existing ones
        ParticleEmitterComponent particleEmitterComponent = view.c1();
        var particleIter = particleEmitterComponent.emittedParticleIterator();

        while (particleIter.hasNext()) {
          var particle = particleIter.next();

          if (particle.getRemainingLifespan() <= 0) {
            particleIter.remove();
          } else {
            particle.update(delta);
          }
        }

        // emit new ones
        particleEmitterComponent.emitIfAble(delta);
      });
  }
}
