package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.physics.component.particle.appearance.ParticleShape;
import technology.sola.engine.physics.component.particle.movement.ParticleNoise;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.linear.Vector2D;

/**
 * Particle contains properties that represent a particle that has been emitted from a {@link ParticleEmitterComponent}.
 */
@NullMarked
public class Particle {
  private final Color baseColor;
  private final ParticleShape shape;
  @Nullable
  private final ParticleNoise noise;
  private final float size;
  private final float inverseMaxLifespan;
  private final Vector2D velocity;
  private final Vector2D position;
  private float remainingLifespan;
  private final float maxLifespan;

  Particle(Color baseColor, ParticleShape shape, @Nullable ParticleNoise noise, float size, float maxLifespan, Vector2D position, Vector2D velocity) {
    this.baseColor = baseColor;
    this.shape = shape;
    this.noise = noise;
    this.size = size;
    this.position = position;
    this.velocity = velocity;

    remainingLifespan = maxLifespan;
    inverseMaxLifespan = 1 / maxLifespan;

    this.maxLifespan = maxLifespan;
  }

  /**
   * Updates the position and lifespan of a particle based on the elapsed delta.
   *
   * @param delta the elapsed delta time
   */
  public void update(float delta) {
    remainingLifespan -= delta;

    if (noise != null) {
      float xStrength = noise.xStrength(); // 25.55f;
      float yStrength = noise.yStrength(); // 0f;
      float frequency = noise.frequency(); // 0.5f;
      float mod = SolaRandom.noise((remainingLifespan) * frequency, ( remainingLifespan) * frequency);
//    float mod = SolaRandom.noise((maxLifespan - remainingLifespan) * frequency, (maxLifespan - remainingLifespan) * frequency);

      Vector2D noiseMod = new Vector2D(
        SolaRandom.nextFloat(-xStrength, xStrength) * mod,
        SolaRandom.nextFloat(-yStrength, yStrength) * mod
      );

      velocity.mutateAdd(noiseMod);
    }


    position.mutateAdd(velocity.scalar(delta));
//    position.mutateAdd(velocity.add(noiseMod).scalar(delta));
//    position.mutateAdd(noiseMod);
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
   * @return the {@link ParticleShape} of the particle
   */
  public ParticleShape getShape() {
    return shape;
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
