package technology.sola.math.geometry;

import org.jspecify.annotations.NullMarked;
import technology.sola.math.linear.Vector2D;

/**
 * ConvexPolygon represents a convex polygon.
 *
 * @param points the points of the convex polygon
 */
@NullMarked
public record ConvexPolygon(Vector2D[] points) implements Shape {
  /**
   * Creates a convex polygon from the provided points.
   *
   * @param points the points of the convex polygon
   * @throws IllegalArgumentException if the provided points do not form a convex polygon
   */
  public ConvexPolygon {
    if (points.length < 3) {
      throw new IllegalArgumentException("A polygon must have at least 3 points");
    }

    // Check if the polygon is convex by verifying all cross-products have the same sign
    Boolean isPositive = null;

    for (int i = 0; i < points.length; i++) {
      Vector2D current = points[i];
      Vector2D next = points[(i + 1) % points.length];
      Vector2D nextNext = points[(i + 2) % points.length];

      // Calculate vectors for consecutive edges
      Vector2D edge1 = next.subtract(current);
      Vector2D edge2 = nextNext.subtract(next);

      // Calculate cross-product (z-component of 3D cross-product)
      float crossProduct = edge1.x() * edge2.y() - edge1.y() * edge2.x();

      // Skip zero cross products (collinear points)
      if (Math.abs(crossProduct) < 1e-9f) {
        continue;
      }

      // Check if this cross-product has the same sign as previous ones
      boolean currentIsPositive = crossProduct > 0;
      if (isPositive == null) {
        isPositive = currentIsPositive;
      } else if (isPositive != currentIsPositive) {
        throw new IllegalArgumentException("The provided points do not form a convex polygon");
      }
    }
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

  /**
   * @return the bounding box of the convex polygon
   */
  public Rectangle boundingBox() {
    var points = points();

    // find width and minX
    float minX = points[0].x();
    float maxX = minX;

    // find height and minY
    float minY = points[0].y();
    float maxY = minY;


    for (int i = 1; i < points.length; i++) {
      var currentPoint = points[i];

      minX = Math.min(minX, currentPoint.x());
      maxX = Math.max(maxX, currentPoint.x());

      minY = Math.min(minY, currentPoint.y());
      maxY = Math.max(maxY, currentPoint.y());
    }

    float width = (maxX - minX);
    float height = (maxY - minY);

    // build Rectangle
    var topLeft = new Vector2D(minX, minY);

    return new Rectangle(
      topLeft,
      topLeft.add(new Vector2D(width, height))
    );
  }
}
