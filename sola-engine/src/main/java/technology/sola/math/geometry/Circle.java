package technology.sola.math.geometry;

import org.jspecify.annotations.NullMarked;
import technology.sola.math.linear.Vector2D;

/**
 * Circle represents a geometric circle.
 *
 * @param radius the radius of the circle
 * @param center the center point of the circle
 */
@NullMarked
public record Circle(float radius, Vector2D center) implements Shape {
  /**
   * Creates a circle with defined radius and center. If the radius provided is zero then a degenerate circle will be
   * created.
   *
   * @param radius the radius of the circle, positive number
   * @param center the center point of the circle
   */
  public Circle {
    if (radius < 0) {
      throw new IllegalArgumentException("radius must be a positive number");
    }
  }

  @Override
  public boolean contains(Vector2D point) {
    return center.distanceSq(point) < radius * radius;
  }

  @Override
  public Vector2D[] getPoints() {
    return new Vector2D[0];
  }

  @Override
  public Vector2D getCentroid() {
    return center();
  }

  @Override
  public float getArea() {
    return (float) (Math.PI * radius * radius);
  }
}
