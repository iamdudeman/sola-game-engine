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

  private float timeSinceLastEmission = Float.MAX_VALUE;
  private int emissionCount = 0;

  /**
   * Emits new particles if enough time has elapsed based on the set properties for emission.
   *
   * @param delta the elapsed delta time
   * @param inheritedVelocity the inherited velocity for emitted particles from the parent {@link technology.sola.ecs.Entity}
   */
  public void emitIfAble(float delta, Vector2D inheritedVelocity) {
    final var emission = this.emission;

    timeSinceLastEmission += delta;

    if (isAbleToEmit(emission)) {
      var speed = SolaRandom.nextFloat(movement.minSpeed(), movement.maxSpeed());
      var appearance = this.appearance;

      for (int i = 0; i < emission.countPerEmit(); i++) {
        var emissionDetails = emission.shape().nextEmission();
        var position = emissionDetails.position();
        var velocity = emissionDetails.direction();

        float xVel = velocity.x() * speed + inheritedVelocity.x() * movement.inheritVelocityPercentage();
        float yVel = velocity.y() * speed + inheritedVelocity.y() * movement.inheritVelocityPercentage();
        float size = SolaRandom.nextFloat(appearance.minSize(), appearance.maxSize());
        float life = SolaRandom.nextFloat(emission.minLife(), emission.maxLife());
        boolean isCircle = SolaRandom.nextFloat() < appearance.percentCircle();
        var shape = isCircle ? Particle.Shape.CIRCLE : Particle.Shape.SQUARE;

        // adjust position to be centered instead of top-left
        position.mutateSubtract(size * 0.5f, size * 0.5f);

        Particle particle = new Particle(
          appearance.color(), shape, size, life, position, new Vector2D(xVel, yVel)
        );

        particleList.add(particle);
      }

      timeSinceLastEmission = 0;
      emissionCount++;
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
  public ParticleMovementConfiguration movementConfig() {
    return movement;
  }

  /**
   * @return object containing emitted {@link Particle} appearance configuration
   */
  public ParticleAppearanceConfiguration appearanceConfig() {
    return appearance;
  }

  /**
   * @return object containing emitted {@link Particle} emission configuration
   */
  public ParticleEmissionConfiguration emissionConfig() {
    return emission;
  }

  private boolean isAbleToEmit(ParticleEmissionConfiguration emission) {
    if (timeSinceLastEmission > emission.interval()) {
      var cycles = emission.cycles();

      return cycles == null || emissionCount < cycles;
    }

    return false;
  }
}
