package technology.sola.engine.physics.component.particle.appearance;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.Color;

@NullMarked
@FunctionalInterface
public interface ParticleColorFunction {
  ParticleColorFunction WHITE = roll -> Color.WHITE;

  /**
   * Returns a {@link Color} based on a random value.
   *
   * @param roll a random value from 0 inclusive to 1 exclusive
   * @return the randomized {@link Color}
   */
  Color getColor(float roll);
}
