package technology.sola.math.geometry;

import technology.sola.engine.annotation.NotNull;
import technology.sola.math.linear.Vector2D;

/**
 * The Circle class represents a geometric circle.
 */
public class Circle {
  private final float radius;
  private final Vector2D center;

  /**
   * Creates a circle with defined radius and center.
   *
   * @param radius  the radius of the circle, greater than 0
   * @param center  the center point of the circle
   */
  public Circle(float radius, @NotNull Vector2D center) {
    if (radius <= 0) {
      throw new IllegalArgumentException("radius must be a positive number");
    }

    if (center == null) {
      throw new IllegalArgumentException("center cannot be null");
    }

    this.radius = radius;
    this.center = center;
  }

  /**
   * Gets the radius.
   *
   * @return the radius
   */
  public float getRadius() {
    return radius;
  }

  /**
   * Gets the center point.
   *
   * @return the center point
   */
  public Vector2D getCenter() {
    return center;
  }
}
