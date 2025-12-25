package technology.sola.engine.physics.component;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ParticleEmitterComponent is a {@link Component} that contains data for emitting {@link Particle}s as well as data for
 * its emitted particles.
 */
@NullMarked
public class ParticleEmitterComponent implements Component {
  private final List<Particle> particleList = new ArrayList<>();
  private Color particleColor = Color.WHITE;
  private float particleMinLife = 1f;
  private float particleMaxLife = 2f;
  private Vector2D particleMinVelocity = new Vector2D(-50, -100);
  private Vector2D particleMaxVelocity = new Vector2D(50, -0.1f);
  private float particleMinSize = 8f;
  private float particleMaxSize = 8f;
  private int particlesPerEmit = 1;
  private float particleEmissionDelay;

  private float timeSinceLastEmission;

  /**
   * Creates a ParticleEmitterComponent instance with default settings.
   */
  public ParticleEmitterComponent() {
    this(0.1f);
  }

  /**
   * Creates a ParticleEmitterComponent instance with default settings and a custom particle emission delay.
   *
   * @param particleEmissionDelay the desired particle emission delay
   */
  public ParticleEmitterComponent(float particleEmissionDelay) {
    this.particleEmissionDelay = particleEmissionDelay;
  }

  /**
   * Emits new particles if enough time has elapsed based on the set properties for emission.
   *
   * @param delta the elapsed delta time
   */
  public void emitIfAble(float delta) {
    timeSinceLastEmission += delta;

    if (timeSinceLastEmission > particleEmissionDelay) {
      Vector2D minVel = particleMinVelocity;
      Vector2D maxVel = particleMaxVelocity;

      for (int i = 0; i < particlesPerEmit; i++) {
        float xVel = SolaRandom.nextFloat(minVel.x(), maxVel.x());
        float yVel = SolaRandom.nextFloat(minVel.y(), maxVel.y());
        float size = SolaRandom.nextFloat(particleMinSize, particleMaxSize);
        float life = SolaRandom.nextFloat(particleMinLife, particleMaxLife);

        Particle particle = new Particle(
          particleColor, size, life, new Vector2D(0, 0), new Vector2D(xVel, yVel)
        );

        particleList.add(particle);
      }

      timeSinceLastEmission = 0;
    }
  }

  /**
   * @return the {@link Iterator} for {@link Particle}s
   */
  public Iterator<Particle> emittedParticleIterator() {
    return particleList.iterator();
  }

  /**
   * Updates the delay between each particle emission.
   *
   * @param particleEmissionDelay the new particle emission delay
   * @return this
   */
  public ParticleEmitterComponent setParticleEmissionDelay(float particleEmissionDelay) {
    this.particleEmissionDelay = particleEmissionDelay;

    return this;
  }

  /**
   * Updates the number of particles to emit per emission.
   *
   * @param particlesPerEmit the new number of particles to emit
   * @return this
   */
  public ParticleEmitterComponent setParticlesPerEmit(int particlesPerEmit) {
    this.particlesPerEmit = particlesPerEmit;

    return this;
  }

  /**
   * Updates the base {@link Color} of each new {@link Particle}.
   *
   * @param particleColor the new color for newly emitted particles
   * @return this
   */
  public ParticleEmitterComponent setParticleColor(Color particleColor) {
    this.particleColor = particleColor;

    return this;
  }

  /**
   * Updates the minimum and maximum lifespans for newly emitted {@link Particle}s.
   *
   * @param particleMinLife the minimum lifespan for a new particle
   * @param particleMaxLife the maximum lifespan for a new particle
   * @return this
   */
  public ParticleEmitterComponent setParticleLifeBounds(float particleMinLife, float particleMaxLife) {
    this.particleMinLife = particleMinLife;
    this.particleMaxLife = particleMaxLife;

    return this;
  }

  /**
   * Updates the lifespan for newly emitted {@link Particle}s to be a fixed value.
   *
   * @param life the lifespan for new particles
   * @return this
   */
  public ParticleEmitterComponent setParticleLife(float life) {
    return setParticleLifeBounds(life, life);
  }

  /**
   * Updates the minimum and maximum velocities for newly emitted {@link Particle}s.
   *
   * @param particleMinVelocity the minimum velocity for new particles
   * @param particleMaxVelocity the maximum velocity for new particles
   * @return this
   */
  public ParticleEmitterComponent setParticleVelocityBounds(Vector2D particleMinVelocity, Vector2D particleMaxVelocity) {
    this.particleMinVelocity = particleMinVelocity;
    this.particleMaxVelocity = particleMaxVelocity;

    return this;
  }

  /**
   * Updates the velocity for newly emitted {@link Particle}s to be a fixed value.
   *
   * @param velocity the velocity for new particles
   * @return this
   */
  public ParticleEmitterComponent setParticleVelocity(Vector2D velocity) {
    setParticleVelocityBounds(velocity, velocity);

    return this;
  }

  /**
   * Updates the minimum and maximum size values for newly emitted {@link Particle}s.
   *
   * @param particleMinSize the minimum size for new particles
   * @param particleMaxSize the maximum size for new particles
   * @return this
   */
  public ParticleEmitterComponent setParticleSizeBounds(float particleMinSize, float particleMaxSize) {
    this.particleMinSize = particleMinSize;
    this.particleMaxSize = particleMaxSize;

    return this;
  }

  /**
   * Updates the size for newly emitted {@link Particle}s to be a fixed value.
   *
   * @param size the size for new particles
   * @return this
   */
  public ParticleEmitterComponent setParticleSize(float size) {
    return setParticleSizeBounds(size, size);
  }

  /**
   * Particle contains properties that represent a particle that has been emitted from a {@link ParticleEmitterComponent}.
   */
  public static class Particle {
    private final Color baseColor;
    private final float size;
    private final float inverseMaxLifespan;
    private final Vector2D velocity;
    private final Vector2D position;
    private float remainingLifespan;

    private Particle(Color baseColor, float size, float maxLifespan, Vector2D position, Vector2D velocity) {
      this.baseColor = baseColor;
      this.size = size;
      this.position = position;
      this.velocity = velocity;

      remainingLifespan = maxLifespan;
      inverseMaxLifespan = 1 / maxLifespan;
    }

    /**
     * Updates the position and lifespan of a particle based on the elapsed delta.
     *
     * @param delta the elapsed delta time
     */
    public void update(float delta) {
      position.mutateAdd(velocity.scalar(delta));
      remainingLifespan -= delta;
    }

    /**
     * @return true if the particle has any remaining lifespan
     */
    public boolean isAlive() {
      return remainingLifespan > 0;
    }

    /**
     * @return the current {@link Color} of the particle based on its origin base color and remaining lifespan
     */
    public Color getColor() {
      int alpha = Math.max((int) ((255 * remainingLifespan * inverseMaxLifespan) + 0.5f), 0);

      return baseColor.updateAlpha(alpha);
    }

    /**
     * @return the current position of the particle
     */
    public Vector2D getPosition() {
      return position;
    }

    /**
     * @return the size of the particle
     */
    public float getSize() {
      return size;
    }
  }
}
