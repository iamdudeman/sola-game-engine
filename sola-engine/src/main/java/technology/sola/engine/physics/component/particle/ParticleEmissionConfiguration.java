package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// todo consider adding support for "pooling" for potential performance improvement

/**
 * ParticleEmissionConfiguration contains configuration for the emission properties for emitting {@link Particle}s.
 */
@NullMarked
public class ParticleEmissionConfiguration extends ParticleConfiguration {
  private float minLife = 1f;
  private float maxLife = 2f;
  private int countPerEmit = 1;
  private float interval = 0.1f;
  @Nullable
  private Integer cycles;

  ParticleEmissionConfiguration(ParticleEmitterComponent owner) {
    super(owner);
  }

  /**
   * @return the minimum lifespan for newly emitted {@link Particle}s
   */
  public float minLife() {
    return minLife;
  }

  /**
   * @return the maximum lifespan for newly emitted {@link Particle}s
   */
  public float maxLife() {
    return maxLife;
  }

  /**
   * @return the interval between each particle emission
   */
  public float interval() {
    return interval;
  }

  /**
   * @return the number of particles to emit per emission
   */
  public int countPerEmit() {
    return countPerEmit;
  }

  /**
   * @return the number of emission cycles before this emitter turns off.
   */
  public @Nullable Integer cycles() {
    return cycles;
  }

  /**
   * Updates the minimum and maximum lifespans for newly emitted {@link Particle}s.
   *
   * @param minLife the minimum lifespan for a new particle
   * @param maxLife the maximum lifespan for a new particle
   * @return this
   */
  public ParticleEmissionConfiguration setLifeBounds(float minLife, float maxLife) {
    this.minLife = minLife;
    this.maxLife = maxLife;

    return this;
  }

  /**
   * Updates the lifespan for newly emitted {@link Particle}s to be a fixed value.
   *
   * @param life the lifespan for new particles
   * @return this
   */
  public ParticleEmissionConfiguration setLife(float life) {
    return setLifeBounds(life, life);
  }

  /**
   * Updates the number of particles to emit per emission.
   *
   * @param countPerEmit the new number of particles to emit
   * @return this
   */
  public ParticleEmissionConfiguration setCountPerEmit(int countPerEmit) {
    this.countPerEmit = countPerEmit;

    return this;
  }

  /**
   * Updates the interval between each particle emission.
   *
   * @param interval the new particle emission interval
   * @return this
   */
  public ParticleEmissionConfiguration setInterval(float interval) {
    this.interval = interval;

    return this;
  }

  /**
   * Updates the number of emission cycles before this emitter turns off. Set cycles to {@code null} to emit particles
   * continuously.
   *
   * @param cycles the number of emission cycles before this emitter turns off.
   * @return this
   */
  public ParticleEmissionConfiguration setCycles(@Nullable Integer cycles) {
    this.cycles = cycles;

    return this;
  }
}
