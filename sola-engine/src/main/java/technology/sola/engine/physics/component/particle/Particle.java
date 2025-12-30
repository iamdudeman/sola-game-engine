package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.Color;
import technology.sola.math.linear.Vector2D;

/**
 * Particle contains properties that represent a particle that has been emitted from a {@link ParticleEmitterComponent}.
 */
@NullMarked
public class Particle {
  private final Color baseColor;
  private final Shape shape;
  private final float size;
  private final float inverseMaxLifespan;
  private final Vector2D velocity;
  private final Vector2D position;
  private float remainingLifespan;

  Particle(Color baseColor, Shape shape, float size, float maxLifespan, Vector2D position, Vector2D velocity) {
    this.baseColor = baseColor;
    this.shape = shape;
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
   * @return the {@link Shape} of the particle
   */
  public Shape getShape() {
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

  public enum Shape {
    CIRCLE,
    SQUARE
  }
}
