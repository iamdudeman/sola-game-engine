package technology.sola.engine.graphics.impl;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.math.SolaMath;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

public class TransformRenderer {
  public static final float UNIT_SIZE = 10f;
  private final Renderer renderer;

  public TransformRenderer(Renderer renderer) {
    this.renderer = renderer;
  }

  public void drawRect(Matrix3D transform, Color color) {
    Vector2D topLeft = transform.forward(0, 0);
    Vector2D topRight = transform.forward(UNIT_SIZE, 0);
    Vector2D bottomRight = transform.forward(UNIT_SIZE, UNIT_SIZE);
    Vector2D bottomLeft = transform.forward(0, UNIT_SIZE);

    renderer.drawLine(topLeft.x, topLeft.y, topRight.x, topRight.y, color);
    renderer.drawLine(topRight.x, topRight.y, bottomRight.x, bottomRight.y, color);
    renderer.drawLine(bottomRight.x, bottomRight.y, bottomLeft.x, bottomLeft.y, color);
    renderer.drawLine(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y, color);
  }

  // TODO improve performance
  public void fillRect(Matrix3D transform, Color color) {
    Vector2D topLeft = transform.forward(0, 0);
    Vector2D topRight = transform.forward(UNIT_SIZE, 0);
    Vector2D bottomRight = transform.forward(UNIT_SIZE, UNIT_SIZE);
    Vector2D bottomLeft = transform.forward(0, UNIT_SIZE);

    float areaOfRect = topLeft.distance(topRight) * topRight.distance(bottomRight);

    float minY = SolaMath.min(topLeft.y, topRight.y, bottomRight.y, bottomLeft.y);
    float maxY = SolaMath.max(topLeft.y, topRight.y, bottomRight.y, bottomLeft.y);
    float minX = SolaMath.min(topLeft.x, topRight.x, bottomRight.x, bottomLeft.x);
    float maxX = SolaMath.max(topLeft.x, topRight.x, bottomRight.x, bottomLeft.x);

    for (float xt = minX; xt <= maxX; xt++) {
      for (float yt = minY; yt <= maxY; yt++) {
        Vector2D point = new Vector2D(xt, yt);

        float sumOfArea = new Triangle(topLeft, point, bottomLeft).getArea()
          + new Triangle(bottomLeft, point, bottomRight).getArea()
          + new Triangle(bottomRight, point, topRight).getArea()
          + new Triangle(point, topRight, topLeft).getArea();

        if (sumOfArea <= areaOfRect + 0.1f) {
          renderer.setPixel((int)(xt + .5f), (int)(yt + .5f), color);
        }
      }
    }
  }
}
