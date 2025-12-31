package technology.sola.engine.physics.system;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.particle.ParticleEmitterComponent;
import technology.sola.math.linear.Vector2D;

/**
 * ParticleSystem is an {@link EcsSystem} that handles updating {@link technology.sola.ecs.Entity} with a
 * {@link TransformComponent} and {@link ParticleEmitterComponent}. It will update each particle that is alive, destroy
 * any particle that is no longer alive, and emit new particles as it is able to.
 */
@NullMarked
public class ParticleSystem extends EcsSystem {
  private static final Vector2D ZERO_VELOCITY = Vector2D.zeroVector();

  @Override
  public void update(World world, float delta) {
    for (var entry : world.createView().of(ParticleEmitterComponent.class, TransformComponent.class).getEntries()) {
      // update existing ones
      ParticleEmitterComponent particleEmitterComponent = entry.c1();
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
      var parentVelocity = getParentVelocity(entry.entity());
      var inheritedVelocity = parentVelocity == null ? ZERO_VELOCITY : parentVelocity;

      particleEmitterComponent.emitIfAble(delta, inheritedVelocity);
    }
  }

  @Nullable
  private Vector2D getParentVelocity(Entity entity) {
    var dynamicBody = entity.getComponent(DynamicBodyComponent.class);

    if (dynamicBody != null) {
      return dynamicBody.getVelocity();
    }

    return null;
  }
}
