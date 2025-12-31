package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;

/**
 * ParticleMovementConfiguration contains configuration for the movement properties of emitted {@link Particle}s.
 */
@NullMarked
public class ParticleMovementConfiguration extends ParticleConfiguration {
  private float minSpeed = 50f;
  private float maxSpeed = 50f;
  private float inheritVelocityPercentage = 0f;

  ParticleMovementConfiguration(ParticleEmitterComponent owner) {
    super(owner);
  }


  public float minSpeed() {
    return minSpeed;
  }

  public float maxSpeed() {
    return maxSpeed;
  }

  public ParticleMovementConfiguration setSpeedBounds(float minSpeed, float maxSpeed) {
    this.minSpeed = minSpeed;
    this.maxSpeed = maxSpeed;

    return this;
  }

  public ParticleMovementConfiguration setSpeed(float speed) {
    this.minSpeed = speed;
    this.maxSpeed = speed;

    return this;
  }


  /**
   * @return the percentage of the owning {@link technology.sola.ecs.Entity}'s velocity to inherit from
   */
  public float inheritVelocityPercentage() {
    return inheritVelocityPercentage;
  }

  /**
   * Updates the percentage of velocity inheritance from the owning {@link technology.sola.ecs.Entity}.
   *
   * @param inheritVelocityPercentage new percentage of velocity inheritance from the owning {@link technology.sola.ecs.Entity}
   * @return this
   */
  public ParticleMovementConfiguration setInheritVelocityPercentage(float inheritVelocityPercentage) {
    this.inheritVelocityPercentage = inheritVelocityPercentage;

    return this;
  }
}
