package technology.sola.math.geometry;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.linear.Vector2D;

import java.util.*;

/**
 * ConvexPolygon represents a convex polygon.
 *
 * @param points the points of the convex polygon
 */
@NullMarked
public record ConvexPolygon(Vector2D[] points) implements Shape {
  /**
   * Creates a random convex polygon using Valtr's Algorithm.
   *
   * @param minSides Minimum sides
   * @param maxSides Maximum sides
   * @param minSize  Minimum radius
   * @param maxSize  Maximum radius
   * @return The polygon
   */
  public static ConvexPolygon createRandom(int minSides, int maxSides, int minSize, int maxSize) {
    var sides = SolaRandom.nextInt(minSides, maxSides);

    // 1. Generate two lists of random X and Y coordinates
    var xPool = new float[sides];
    var yPool = new float[sides];

    for (int i = 0; i < sides; i++) {
      xPool[i] = SolaRandom.nextFloat(minSize, maxSize);
      yPool[i] = SolaRandom.nextFloat(minSize, maxSize);
    }

    // 2. Sort the coordinates
    Arrays.sort(xPool);
    Arrays.sort(yPool);

    // 3. Isolate the extreme points
    var minX = xPool[0];
    var maxX = xPool[sides - 1];
    var minY = yPool[0];
    var maxY = yPool[sides - 1];

    // 4. Divide the interior points into two chains and compute differences (vectors)
    List<Float> xVec = new ArrayList<>(sides);
    List<Float> yVec = new ArrayList<>(sides);

    var lastTop = minX;
    var lastBot = minX;

    for (int i = 1; i < sides - 1; i++) {
      float x = xPool[i];

      if (SolaRandom.nextBoolean()) {
        xVec.add(x - lastTop);
        lastTop = x;
      } else {
        xVec.add(lastBot - x);
        lastBot = x;
      }
    }

    xVec.add(maxX - lastTop);
    xVec.add(lastBot - maxX);

    var lastLeft = minY;
    var lastRight = minY;

    for (int i = 1; i < sides - 1; i++) {
      float y = yPool[i];

      if (SolaRandom.nextBoolean()) {
        yVec.add(y - lastLeft);
        lastLeft = y;
      } else {
        yVec.add(lastRight - y);
        lastRight = y;
      }
    }

    yVec.add(maxY - lastLeft);
    yVec.add(lastRight - maxY);

    // 5. Randomly pair up the X and Y components
    Collections.shuffle(yVec);

    // 6. Combine the paired components into vectors and sort them by angle
    List<Vector2D> vectors = new ArrayList<>(sides);

    for (int i = 0; i < sides; i++) {
      vectors.add(new Vector2D(xVec.get(i), yVec.get(i)));
    }

    vectors.sort(Comparator.comparingDouble(v -> Math.atan2(v.y(), v.x())));

    // 7. Lay the vectors end-to-end to form the polygon
    var points = new Vector2D[sides];
    var x = 0f;
    var y = 0f;
    var minPolygonX = 0f;
    var minPolygonY = 0f;

    for (int i = 0; i < sides; i++) {
      points[i] = new Vector2D(x, y);
      x += vectors.get(i).x();
      y += vectors.get(i).y();

      minPolygonX = Math.min(minPolygonX, x);
      minPolygonY = Math.min(minPolygonY, y);
    }

    // 8. Shift the polygon back to the original minSize bounding box
    var xShift = minX - minPolygonX;
    var yShift = minY - minPolygonY;

    for (int i = 0; i < sides; i++) {
      points[i].mutateAdd(xShift, yShift);
    }

    return new ConvexPolygon(points);
  }

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
