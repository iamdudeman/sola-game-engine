package technology.sola.engine.physics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;

/**
 * ParticleSystem is an {@link EcsSystem} that handles updating {@link technology.sola.ecs.Entity} with a
 * {@link TransformComponent} and {@link ParticleEmitterComponent}. It will update each particle that is alive, destroy
 * any particle that is no longer alive, and emit new particles as it is able to.
 */
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

          if (particle.isAlive()) {
            particle.update(delta);
          } else {
            particleIter.remove();
          }
        }

        // emit new ones
        particleEmitterComponent.emitIfAble(delta);
      });
  }
}
