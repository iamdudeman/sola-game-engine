package technology.sola.math.geometry;

import org.jspecify.annotations.NullMarked;
import technology.sola.math.linear.Vector2D;

/**
 * Shape is a common interface for geometric shapes.
 */
@NullMarked
public interface Shape {
  /**
   * Checks if this Shape contains a point.
   *
   * @param point the point to check
   * @return true if this shape contains the point
   */
  boolean contains(Vector2D point);

  /**
   * @return the array of points making up this shape
   */
  Vector2D[] points();

  /**
   * Calculates the area of this shape and return it.
   *
   * @return the area of this shape
   */
  float getArea();

  /**
   * Calculates and returns the centroid of this {@link Shape}.
   *
   * @return the centroid
   */
  default Vector2D getCentroid() {
    return calculateCentroid(points());
  }

  /**
   * Calculates and returns the centroid of the shape represented by the array of points.
   *
   * @param points the points of the shape
   * @return the centroid
   */
  static Vector2D calculateCentroid(Vector2D[] points) {
    float xSum = 0;
    float ySum = 0;

    for (Vector2D point : points) {
      xSum += point.x();
      ySum += point.y();
    }

    return new Vector2D(xSum / points.length, ySum / points.length);
  }
}
