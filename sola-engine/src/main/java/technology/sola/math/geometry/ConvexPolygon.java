package technology.sola.math.geometry;

import org.jspecify.annotations.NullMarked;
import technology.sola.math.linear.Vector2D;

@NullMarked
public record ConvexPolygon(Vector2D[] points) implements Shape {
  public ConvexPolygon {
    // todo validate it is a convex polygon
  }

  @Override
  public boolean contains(Vector2D point) {
    // Note: implementation from https://stackoverflow.com/a/34689268
    int positives = 0;
    int negatives = 0;

    for (int i = 0; i < points.length; i++) {
      var current = points[i];

      if (current.equals(point)) {
        return true;
      }

      var x1 = current.x();
      var y1 = current.y();
      var i2 = (i + points.length - 1) % points.length;
      var x2 = points[i2].x();
      var y2 = points[i2].y();
      var x = point.x();
      var y = point.y();

      var crossProduct = (x - x1) * (y2 - y1) - (y - y1) * (x2 - x1);

      if (crossProduct > 0) {
        positives++;
      }
      if (crossProduct < 0) {
        negatives++;
      }

      if (positives > 0 && negatives > 0) {
        return false;
      }
    }

    return true;
  }

  @Override
  public float getArea() {
    float area = 0;

    for (int i = 0; i < points.length; i++) {
      var current = points[i];
      var next = points[(i + 1) % points.length];

      area += (current.y() + next.y()) * (next.x() - current.x());
    }

    return Math.abs(area) * 0.5f;
  }
}
