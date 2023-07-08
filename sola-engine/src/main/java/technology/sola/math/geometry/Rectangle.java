package technology.sola.math.geometry;

import technology.sola.math.linear.Vector2D;

/**
 * Rectangle represents an axis aligned geometric rectangle.
 *
 * @param min the top, left point of the rectangle
 * @param max the bottom, right point of the rectangle
 */
public record Rectangle(Vector2D min, Vector2D max) {
  /**
   * Creates a rectangle by min and max points.
   * <p>
   * The min point's x and y must both be below the max point's x and y. If they are the same then a degenerate rectangle
   * with width or height of zero will be created.
   *
   * @param min the top, left point of the rectangle
   * @param max the bottom, right point of the rectangle
   */
  public Rectangle {
    if (max.x() < min.x()) {
      throw new IllegalArgumentException("max.x cannot be less than min.x");
    }

    if (max.y() < min.y()) {
      throw new IllegalArgumentException("max.y cannot be less than min.y");
    }
  }

  /**
   * Gets the width of the rectangle.
   *
   * @return the width
   */
  public float getWidth() {
    return max.subtract(min).x();
  }

  /**
   * Gets the height of the rectangle.
   *
   * @return the height
   */
  public float getHeight() {
    return max.subtract(min).y();
  }

  /**
   * Checks if this Rectangle contains a point.
   *
   * @param point the point to check
   * @return true if this rectangle contains the point
   */
  public boolean contains(Vector2D point) {
    return point.x() >= min.x() && point.x() <= max.x() && point.y() >= min.y() && point.y() <= max.y();
  }
}
