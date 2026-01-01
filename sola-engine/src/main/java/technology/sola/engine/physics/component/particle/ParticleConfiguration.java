package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;

/**
 * ParticleConfiguration defines the api for an object that holds configuration for emitting {@link Particle}s.
 */
@NullMarked
public abstract class ParticleConfiguration {
  private final ParticleEmitterComponent owner;

  ParticleConfiguration(ParticleEmitterComponent owner) {
    this.owner = owner;
  }

  /**
   * Called to get the instance of the owning {@link ParticleEmitterComponent} when configuration is completed.
   *
   * @return the owning {@link ParticleEmitterComponent}
   */
  public ParticleEmitterComponent done() {
    return owner;
  }
}
