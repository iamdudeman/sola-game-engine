package technology.sola.engine.physics.component.particle.appearance;

import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface ParticleShapeFunction {
  ParticleShapeFunction CIRCLE = roll -> ParticleShape.CIRCLE;

  /**
   * Returns a {@link ParticleShape} based on a random value.
   *
   * @param roll a random value from 0 inclusive to 1 exclusive
   * @return the randomized {@link ParticleShape}
   */
  ParticleShape getShape(float roll);
}
