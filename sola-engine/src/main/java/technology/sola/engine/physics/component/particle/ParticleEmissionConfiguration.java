package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.physics.component.particle.emitter.CircleEmitterShape;
import technology.sola.engine.physics.component.particle.emitter.ParticleEmitterShape;

/**
 * ParticleEmissionConfiguration contains configuration for the emission properties for emitting {@link Particle}s.
 */
@NullMarked
public class ParticleEmissionConfiguration extends ParticleConfiguration {
  private ParticleEmitterShape shape = new CircleEmitterShape(50);
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
   * @return the {@link ParticleEmitterShape} for this emitter. Defaults to {@link CircleEmitterShape}
   */
  public ParticleEmitterShape shape() {
    return shape;
  }

  /**
   * Updates the {@link ParticleEmitterShape} for this emitter.
   *
   * @param shape the new shape
   * @return this
   */
  public ParticleEmissionConfiguration setShape(ParticleEmitterShape shape) {
    this.shape = shape;

    return this;
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
   * @return the interval between each particle emission in seconds
   */
  public float interval() {
    return interval;
  }

  /**
   * Updates the interval between each particle emission.
   *
   * @param interval the new particle emission interval (in seconds)
   * @return this
   */
  public ParticleEmissionConfiguration setInterval(float interval) {
    this.interval = interval;

    return this;
  }


  /**
   * @return the number of particles to emit per emission
   */
  public int countPerEmit() {
    return countPerEmit;
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
   * @return the number of emission cycles before this emitter turns off.
   */
  public @Nullable Integer cycles() {
    return cycles;
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
