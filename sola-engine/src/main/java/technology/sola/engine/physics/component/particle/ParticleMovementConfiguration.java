package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.physics.component.particle.movement.ParticleNoise;

/**
 * ParticleMovementConfiguration contains configuration for the movement properties of emitted {@link Particle}s.
 */
@NullMarked
public class ParticleMovementConfiguration extends ParticleConfiguration {
  private float minSpeed = 50f;
  private float maxSpeed = 50f;
  private float inheritedVelocityPercentage = 0f;
  @Nullable
  private ParticleNoise noise;

  ParticleMovementConfiguration(ParticleEmitterComponent owner) {
    super(owner);
  }


  /**
   * @return the minimum speed for newly emitted {@link Particle}s
   */
  public float minSpeed() {
    return minSpeed;
  }

  /**
   * @return the maximum speed for newly emitted {@link Particle}s
   */
  public float maxSpeed() {
    return maxSpeed;
  }

  /**
   * Updates the minimum and maximum speed values for newly emitted {@link Particle}s.
   *
   * @param minSpeed the minimum speed for new particles
   * @param maxSpeed the maximum speed for new particles
   * @return this
   */
  public ParticleMovementConfiguration setSpeedBounds(float minSpeed, float maxSpeed) {
    this.minSpeed = minSpeed;
    this.maxSpeed = maxSpeed;

    return this;
  }

  /**
   * Sets the speed for newly emitted {@link Particle}s.
   *
   * @param speed the speed for new particles
   * @return this
   */
  public ParticleMovementConfiguration setSpeed(float speed) {
    this.minSpeed = speed;
    this.maxSpeed = speed;

    return this;
  }


  /**
   * @return the percentage of the owning {@link technology.sola.ecs.Entity}'s velocity to inherit from
   */
  public float inheritedVelocityPercentage() {
    return inheritedVelocityPercentage;
  }

  /**
   * Updates the percentage of velocity inheritance from the owning {@link technology.sola.ecs.Entity}.
   *
   * @param inheritedVelocityPercentage new percentage of velocity inheritance from the owning {@link technology.sola.ecs.Entity}
   * @return this
   */
  public ParticleMovementConfiguration setInheritedVelocityPercentage(float inheritedVelocityPercentage) {
    this.inheritedVelocityPercentage = inheritedVelocityPercentage;

    return this;
  }


  public @Nullable ParticleNoise noise() {
    return noise;
  }

  public ParticleMovementConfiguration setNoise(@Nullable ParticleNoise noise) {
    this.noise = noise;

    return this;
  }
}
