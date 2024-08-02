package technology.sola.engine.physics.utils;

import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Shape;
import technology.sola.math.linear.Vector2D;

/**
 * SeparatingAxisTheorem contains methods for checking whether two convex polygons are colliding or not.
 */
public class SeparatingAxisTheorem {
  /**
   * Checks for collisions between to shapes. If a collision is detected the {@link MinimumTranslationVector} will be
   * returned. If no collision is found then null will be returned.
   *
   * @param shapeA the first array of points of a shape to check collisions with
   * @param shapeB the second array of points of a shape to check collisions with
   * @return the {@code MinimumTranslationVector} if a collision was found or else null
   */
  public static MinimumTranslationVector checkCollision(Vector2D[] shapeA, Vector2D[] shapeB) {
    float smallestOverlap = Float.MAX_VALUE;
    Vector2D smallestAxis = null;
    Vector2D[] axes = new Vector2D[shapeA.length + shapeB.length];

    System.arraycopy(getAxes(shapeA), 0, axes, 0, shapeA.length);
    System.arraycopy(getAxes(shapeB), 0, axes, shapeA.length, shapeB.length);

    for (Vector2D axis : axes) {
      Projection p1 = projectShapeToAxis(shapeA, axis);
      Projection p2 = projectShapeToAxis(shapeB, axis);

      if (p1.isOverlapping(p2)) {
        float overlap = p1.getOverlap(p2);

        if (overlap < smallestOverlap) {
          smallestOverlap = overlap;
          smallestAxis = axis;
        }
      } else {
        return null;
      }
    }

    Vector2D centroidA = Shape.calculateCentroid(shapeA);
    Vector2D centroidB = Shape.calculateCentroid(shapeB);

    if (centroidB.subtract(centroidA).dot(smallestAxis) < 0) {
      smallestAxis = smallestAxis.scalar(-1);
    }

    return new MinimumTranslationVector(smallestAxis, smallestOverlap);
  }

  /*
  todo try this algorithm to see if it works for shapeXcircle
  1. You get the closest point from the circle center to the polygon
  2. Get the distance from the closest point to the circle center
  3. Normalize that distance to get the unit vector
  4. Get the signed distance to the edge/vertex and take the radius into account
  5. Multiply your unit vector by the signed distance and you get the minimum translation vector
   */

  // todo return normal + penetration or null
  public static MinimumTranslationVector checkCollision(Vector2D[] shape, Circle circle) {
    // todo implement
    var circleCenter = circle.center();
    var shapeCentroid = Shape.calculateCentroid(shape);

    return null;
  }

  private static Projection projectShapeToAxis(Vector2D[] shape, Vector2D axis) {
    float min = axis.dot(shape[0]);
    float max = min;

    for (int i = 1; i < shape.length; i++) {
      float value = axis.dot(shape[i]);

      if (value < min) {
        min = value;
      } else if (value > max) {
        max = value;
      }
    }

    return new Projection(min, max);
  }

  private static Vector2D[] getAxes(Vector2D[] shape) {
    Vector2D[] axes = new Vector2D[shape.length];

    for (int i = 0; i < shape.length; i++) {
      Vector2D p1 = shape[i];
      int nextIndex = i + 1 == shape.length ? 0 : i + 1;
      Vector2D p2 = shape[nextIndex];
      Vector2D edge = p1.subtract(p2);
      // perpendicular line
      Vector2D normal = new Vector2D(-edge.y(), edge.x());

      axes[i] = normal.normalize();
    }

    return axes;
  }

  /**
   * The minimum translation vector contains the information needed to resolve a collision.
   *
   * @param normal      the collision normal
   * @param penetration the penetration of the collision
   */
  public record MinimumTranslationVector(Vector2D normal, float penetration) {
  }

  private record Projection(float min, float max) {
    boolean isOverlapping(Projection projection) {
      return projection.max >= min && max >= projection.min;
    }

    float getOverlap(Projection projection) {
      return Math.max(0, Math.min(max, projection.max) - Math.max(min, projection.min));
    }
  }
}
