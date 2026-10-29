package technology.sola.math.geometry;

import org.jspecify.annotations.NullMarked;
import technology.sola.math.linear.Vector2D;

/**
 * Ellipse represents a geometric ellipse.
 *
 * @param width  the width of the ellipse
 * @param height the height of the ellipse
 */
@NullMarked
public record Ellipse(float width, float height) implements Shape {
  /**
   * Creates an ellipse with defined width and height. If the width or height provided is zero, then a degenerate
   * ellipse will be created.
   *
   * @param width  the width of the ellipse
   * @param height the height of the ellipse
   */
  public Ellipse {
    if (width < 0) {
      throw new IllegalArgumentException("width must be a positive number");
    }

    if (height < 0) {
      throw new IllegalArgumentException("height must be a positive number");
    }
  }

  @Override
  public boolean contains(Vector2D point) {
    // Uses: ((x - centerX)^2 / radiusX^2) + ((y - centerY)^2 / radiusY^2) < 1
    float radiusX = width * 0.5f;
    float radiusY = height * 0.5f;

    if (radiusX == 0 || radiusY == 0) {
      return false;
    }

    Vector2D centroid = getCentroid();
    float normalizedX = (point.x() - centroid.x()) / radiusX;
    float normalizedY = (point.y() - centroid.y()) / radiusY;

    return normalizedX * normalizedX + normalizedY * normalizedY < 1;
  }

  @Override
  public Vector2D[] points() {
    return new Vector2D[0];
  }

  @Override
  public Vector2D getCentroid() {
    return new Vector2D(width * 0.5f, height * 0.5f);
  }

  @Override
  public float getArea() {
    // Uses: π * radiusX * radiusY
    return (float) (Math.PI * width * height * 0.25f);
  }
}
