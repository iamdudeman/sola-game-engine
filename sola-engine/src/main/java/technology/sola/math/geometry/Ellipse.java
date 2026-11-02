package technology.sola.math.geometry;

import org.jspecify.annotations.NullMarked;
import technology.sola.math.linear.Vector2D;

/**
 * Ellipse represents a geometric ellipse.
 *
 * @param center  the center point of the ellipse
 * @param radiusX the radius on the x-axis
 * @param radiusY the radius on the y-axis
 */
@NullMarked
public record Ellipse(Vector2D center, float radiusX, float radiusY) implements Shape {
  /**
   * Number of approximation segments used to approximate the ellipse (useful for collision detection).
   * <pre>
   * 8 - rough, octagon-like
   * 16 - often acceptable
   * 32 - good for many games
   * 64 - smoother, more accurate
   * 128+ - high accuracy but more expensive
   * </pre>
   */
  public static int APPROXIMATION_SEGMENTS = 24;

  /**
   * Creates an ellipse with defined center point, and horizontal and vertical radius. If either radius provided is
   * zero, then a degenerate ellipse will be created.
   *
   * @param center  the center point of the ellipse
   * @param radiusX the radius on the x-axis
   * @param radiusY the radius on the y-axis
   */
  public Ellipse {
    if (radiusX < 0) {
      throw new IllegalArgumentException("radiusX must be a positive number");
    }

    if (radiusY < 0) {
      throw new IllegalArgumentException("radiusY must be a positive number");
    }
  }

  @Override
  public boolean contains(Vector2D point) {
    if (radiusX == 0 || radiusY == 0) {
      return false;
    }

    // Uses: ((x - centerX)^2 / radiusX^2) + ((y - centerY)^2 / radiusY^2) < 1
    float normalizedX = (point.x() - center.x()) / radiusX;
    float normalizedY = (point.y() - center.y()) / radiusY;

    return normalizedX * normalizedX + normalizedY * normalizedY < 1;
  }

  /**
   * Returns the approximate points of the ellipse using {@link Ellipse#APPROXIMATION_SEGMENTS} segments.
   *
   * @return the approximate points of the ellipse
   */
  @Override
  public Vector2D[] points() {
    return points(APPROXIMATION_SEGMENTS);
  }

  /**
   * Returns the approximate points of the ellipse based on the number of desired segments.
   *
   * <pre>
   * 8 - rough, octagon-like
   * 16 - often acceptable
   * 32 - good for many games
   * 64 - smoother, more accurate
   * 128+ - high accuracy but more expensive
   * </pre>
   *
   * @param segments the number of segments to approximate the ellipse
   * @return the approximate points of the ellipse
   */
  public Vector2D[] points(int segments) {
    Vector2D[] points = new Vector2D[segments];

    for (int i = 0; i < segments; i++) {
      double angle = 2.0 * Math.PI * i / segments;

      float x = center.x() + (float) Math.cos(angle) * radiusX;
      float y = center.y() + (float) Math.sin(angle) * radiusY;

      points[i] = new Vector2D(x, y);
    }

    return points;
  }

  @Override
  public Vector2D getCentroid() {
    return center;
  }

  @Override
  public float getArea() {
    // Uses: π * radiusX * radiusY
    return (float) (Math.PI * radiusX * radiusY);
  }
}
