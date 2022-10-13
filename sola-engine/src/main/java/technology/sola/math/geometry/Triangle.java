package technology.sola.math.geometry;

import technology.sola.math.linear.Vector2D;

public class Triangle {
  private final Vector2D edgeA;
  private final Vector2D edgeB;
  private final Vector2D edgeC;

  public Triangle(Vector2D edgeA, Vector2D edgeB, Vector2D edgeC) {
    this.edgeA = edgeA;
    this.edgeB = edgeB;
    this.edgeC = edgeC;
  }

  public float getArea() {
    return Math.abs(
      (edgeB.x() * edgeA.y() - edgeA.x() * edgeB.y())
        + (edgeC.x() * edgeB.y() - edgeB.x() * edgeC.y())
        + (edgeA.x() * edgeC.y() - edgeC.x() * edgeA.y())
    ) * 0.5f;
  }
}
