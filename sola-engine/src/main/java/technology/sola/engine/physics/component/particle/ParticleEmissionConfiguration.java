package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// todo consider adding support for "pooling" for potential performance improvement

@NullMarked
public class ParticleEmissionConfiguration extends ParticleConfiguration {
  private float minLife = 1f;
  private float maxLife = 2f;
  private int countPerEmit = 1;

  private float interval = 0.1f; // todo is new so needs to be hooked up

  @Nullable
  private Integer cycles; // todo is new so needs to be hooked up

  ParticleEmissionConfiguration(ParticleEmitterComponent owner) {
    super(owner);
  }

  public float minLife() {
    return minLife;
  }

  public float maxLife() {
    return maxLife;
  }

  public int countPerEmit() {
    return countPerEmit;
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
}
