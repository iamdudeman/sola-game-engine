package technology.sola.math.geometry;

import technology.sola.math.linear.Vector2D;

/**
 * Circle represents a geometric circle.
 */
public record Circle(float radius, Vector2D center) {
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

    if (center == null) {
      throw new IllegalArgumentException("center cannot be null");
    }
  }

  /**
   * Gets the radius.
   *
   * @return the radius
   */
  @Override
  public float radius() {
    return radius;
  }

  /**
   * Gets the center point.
   *
   * @return the center point
   */
  @Override
  public Vector2D center() {
    return center;
  }
}
