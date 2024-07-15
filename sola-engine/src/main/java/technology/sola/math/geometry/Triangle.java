package technology.sola.math.geometry;

import technology.sola.math.linear.Vector2D;

/**
 * Triangle represents a geometric triangle.
 *
 * @param p1 first point of triangle
 * @param p2 second point of triangle
 * @param p3 third point of triangle
 */
public record Triangle(Vector2D p1, Vector2D p2, Vector2D p3) implements Shape {
  // Note: implementation from https://stackoverflow.com/a/2049593
  @Override
  public boolean contains(Vector2D point) {
    float d1 = sign(point, p1, p2);
    float d2 = sign(point, p2, p3);
    float d3 = sign(point, p3, p1);
    boolean hasNegative = (d1 < 0) || (d2 < 0) || (d3 < 0);
    boolean hasPositive = (d1 > 0) || (d2 > 0) || (d3 > 0);

    return !(hasNegative && hasPositive);
  }

  /**
   * Calculates the area of this triangle and return it.
   *
   * @return the area of this triangle
   */
  public float getArea() {
    return Math.abs(
      (p2.x() * p1.y() - p1.x() * p2.y())
        + (p3.x() * p2.y() - p2.x() * p3.y())
        + (p1.x() * p3.y() - p3.x() * p1.y())
    ) * 0.5f;
  }

  public Vector2D getCentroid() {
    float x = (p1.x() + p2.x() + p3.x()) / 3;
    float y = (p1.y() + p2.y() + p3.y()) / 3;

    return new Vector2D(x, y);
  }

  private float sign(Vector2D p1, Vector2D p2, Vector2D p3) {
    return (p1.x() - p3.x()) * (p2.y() - p3.y()) - (p2.x() - p3.x()) * (p1.y() - p3.y());
  }
}
