package technology.sola.math.geometry;

import technology.sola.math.linear.Vector2D;

/**
 * Shape is a common interface for geometric shapes.
 */
public interface Shape {
  /**
   * Checks if this Shape contains a point.
   *
   * @param point the point to check
   * @return true if this shape contains the point
   */
  boolean contains(Vector2D point);
}
