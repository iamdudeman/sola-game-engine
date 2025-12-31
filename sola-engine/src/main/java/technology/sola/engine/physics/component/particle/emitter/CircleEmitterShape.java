package technology.sola.engine.physics.component.particle.emitter;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.linear.Vector2D;

/**
 * CircleEmitterShape is a {@link ParticleEmitterShape} that emits particles in the shape of a circle.
 */
@NullMarked
public class CircleEmitterShape extends ParticleEmitterShape {
  private float radius;
  private Vector2D center;

  /**
   * Creates a CircleEmitterShape with the desired radius.
   *
   * @param radius the radius of the circle
   */
  public CircleEmitterShape(float radius) {
    setRadius(radius);
  }

  /**
   * @return the radius of the circle
   */
  public float getRadius() {
    return radius;
  }

  /**
   * Sets the radius of the emission circle.
   *
   * @param radius the new radius of the emission circle
   * @return this
   */
  public CircleEmitterShape setRadius(float radius) {
    if (radius <= 0) {
      throw new IllegalArgumentException("radius must be greater than 0");
    }

    this.radius = radius;

    center = new Vector2D(radius, radius);

    return this;
  }

  @Override
  protected Vector2D getCenter() {
    return center;
  }

  @Override
  protected Vector2D randomPointInShape() {
    var r = radius * (float) Math.sqrt(SolaRandom.nextFloat());

    return getRandomPoint(r);
  }

  @Override
  protected Vector2D randomPointOnPerimeter() {
    return getRandomPoint(radius);
  }

  private Vector2D getRandomPoint(float radius) {
    var angle = SolaRandom.nextFloat() * 2 * (float) Math.PI;

    return new Vector2D(
      center.x() + radius * (float) Math.cos(angle),
      center.y() + radius * (float) Math.sin(angle)
    );
  }
}
