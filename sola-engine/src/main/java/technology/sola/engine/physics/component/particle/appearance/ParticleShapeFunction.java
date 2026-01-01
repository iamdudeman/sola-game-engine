package technology.sola.engine.physics.component.particle.appearance;

import org.jspecify.annotations.NullMarked;

/**
 * ParticleShapeFunction is a function that returns a {@link ParticleShape} based on a random value.
 */
@NullMarked
@FunctionalInterface
public interface ParticleShapeFunction {
  /**
   * A {@link ParticleShapeFunction} that always returns {@link ParticleShape#CIRCLE}.
   */
  ParticleShapeFunction CIRCLE = roll -> ParticleShape.CIRCLE;

  /**
   * Returns a {@link ParticleShape} based on a random value.
   *
   * @param roll a random value from 0 inclusive to 1 exclusive
   * @return the randomized {@link ParticleShape}
   */
  ParticleShape getShape(float roll);
}
