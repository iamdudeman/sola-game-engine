package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
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
  private final ParticleMovementConfiguration movement = new ParticleMovementConfiguration(this);
  private final ParticleAppearanceConfiguration appearance = new ParticleAppearanceConfiguration(this);
  private final ParticleEmissionConfiguration emission = new ParticleEmissionConfiguration(this);
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
      Vector2D minVel = movement.minVelocity();
      Vector2D maxVel = movement.maxVelocity();
      var appearance = this.appearance;
      var emission = this.emission;

      for (int i = 0; i < emission.countPerEmit(); i++) {
        float xVel = SolaRandom.nextFloat(minVel.x(), maxVel.x());
        float yVel = SolaRandom.nextFloat(minVel.y(), maxVel.y());
        float size = SolaRandom.nextFloat(appearance.minSize(), appearance.maxSize());
        float life = SolaRandom.nextFloat(emission.minLife(), emission.maxLife());

        Particle particle = new Particle(
          appearance.color(), size, life, new Vector2D(0, 0), new Vector2D(xVel, yVel)
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
   * @return object containing emitted {@link Particle} movement configuration
   */
  public ParticleMovementConfiguration configureMovement() {
    return movement;
  }

  /**
   * @return object containing emitted {@link Particle} appearance configuration
   */
  public ParticleAppearanceConfiguration configureAppearance() {
    return appearance;
  }

  /**
   * @return object containing emitted {@link Particle} emission configuration
   */
  public ParticleEmissionConfiguration configureEmission() {
    return emission;
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
}
