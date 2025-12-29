package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import technology.sola.math.linear.Vector2D;

// todo investigate the concept of "noise" in movement

/**
 * ParticleMovementConfiguration contains configuration for the movement properties of emitted {@link Particle}s.
 */
@NullMarked
public class ParticleMovementConfiguration extends ParticleConfiguration {
  private Vector2D minVelocity = new Vector2D(-50, -100);
  private Vector2D maxVelocity = new Vector2D(50, -0.1f);

  ParticleMovementConfiguration(ParticleEmitterComponent owner) {
    super(owner);
  }

  /**
   * @return the minimum velocity boundary for newly emitted {@link Particle}s
   */
  public Vector2D minVelocity() {
    return minVelocity;
  }

  /**
   * @return the maximum velocity boundary for newly emitted {@link Particle}s
   */
  public Vector2D maxVelocity() {
    return maxVelocity;
  }

  /**
   * Updates the velocity for newly emitted {@link Particle}s to be a fixed value.
   *
   * @param velocity the velocity for new particles
   * @return this
   */
  public ParticleMovementConfiguration setVelocity(Vector2D velocity) {
    return setVelocityBounds(velocity, velocity);
  }

  /**
   * Updates the minimum and maximum velocities for newly emitted {@link Particle}s.
   *
   * @param min the minimum velocity for new particles
   * @param max the maximum velocity for new particles
   * @return this
   */
  public ParticleMovementConfiguration setVelocityBounds(Vector2D min, Vector2D max) {
    this.minVelocity = min;
    this.maxVelocity = max;

    return this;
  }
}
