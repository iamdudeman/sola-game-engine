package technology.sola.math.geometry;

import technology.sola.math.linear.Vector2D;

/**
 * Triangle represents a geometric triangle.
 *
 * @param edgeA first side of triangle
 * @param edgeB second side of triangle
 * @param edgeC third side of triangle
 */
public record Triangle(Vector2D edgeA, Vector2D edgeB, Vector2D edgeC) {
  /**
   * Calculates the area of this triangle and return it.
   *
   * @return the area of this triangle
   */
  public float getArea() {
    return Math.abs(
      (edgeB.x() * edgeA.y() - edgeA.x() * edgeB.y())
        + (edgeC.x() * edgeB.y() - edgeB.x() * edgeC.y())
        + (edgeA.x() * edgeC.y() - edgeC.x() * edgeA.y())
    ) * 0.5f;
  }
}
