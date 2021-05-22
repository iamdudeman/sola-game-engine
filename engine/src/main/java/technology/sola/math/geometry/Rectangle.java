package technology.sola.math.geometry;

import technology.sola.math.linear.Vector2D;

/**
 * The Rectangle class represents a geometric rectangle.
 */
public class Rectangle {
  private final Vector2D min;
  private final Vector2D max;

  /**
   * Creates a rectangle by min and max points.
   * <p>
   * The min point's x and y must both be below the max point's x and y.
   *
   * @param min  the top, left point of the rectangle
   * @param max  the bottom, right point of the rectangle
   */
  public Rectangle(Vector2D min, Vector2D max) {
    if (max.x <= min.x) {
      throw new IllegalArgumentException("max.x cannot be less than min.x");
    }

    if (max.y <= min.y) {
      throw new IllegalArgumentException("max.y cannot be less than min.y");
    }

    this.min = min;
    this.max = max;
  }

  /**
   * Gets the top, left point of the rectangle.
   *
   * @return the min point
   */
  public Vector2D getMin() {
    return min;
  }

  /**
   * Gets the bottom, right point of the rectangle.
   *
   * @return the max point
   */
  public Vector2D getMax() {
    return max;
  }

  /**
   * Gets the width of the rectangle.
   *
   * @return the width
   */
  public float getWidth() {
    return max.subtract(min).x;
  }

  /**
   * Gets the height of the rectangle.
   *
   * @return the height
   */
  public float getHeight() {
    return max.subtract(min).y;
  }
}
