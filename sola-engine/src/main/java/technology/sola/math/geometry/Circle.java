package technology.sola.math.geometry;

import technology.sola.math.linear.Vector2D;

/**
 * Circle represents a geometric circle.
 *
 * @param radius the radius of the circle
 * @param center the center point of the circle
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
   * Checks if this Circle contains a point.
   *
   * @param point the point to check
   * @return true if this circle contains the point
   */
  public boolean contains(Vector2D point) {
    return center.subtract(point).magnitudeSq() < radius * radius;
  }
}
