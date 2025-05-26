package technology.sola.engine.physics.utils;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.physics.MinimumTranslationVector;
import technology.sola.math.geometry.Shape;
import technology.sola.math.linear.Vector2D;

/**
 * SeparatingAxisTheorem contains methods for checking whether two convex polygons are colliding or not.
 */
@NullMarked
public class SeparatingAxisTheorem {
  /**
   * Checks for collisions between to shapes. If a collision is detected the {@link MinimumTranslationVector} will be
   * returned. If no collision is found then null will be returned.
   *
   * @param shapeA the first array of points of a shape to check collisions with
   * @param shapeB the second array of points of a shape to check collisions with
   * @return the {@code MinimumTranslationVector} if a collision was found or else null
   */
  @Nullable
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

  /**
   * Checks for collisions between a shape and a circle. If a collision is detected
   * the {@link MinimumTranslationVector} will be returned. If no collision is found then null will be returned.
   *
   * @param shape        the array of points of a shape to check collisions with
   * @param circleCenter the circle center to check collisions with
   * @param radius       the circle radius to check collisions with
   * @return the {@code MinimumTranslationVector} if a collision was found or else null
   */
  @Nullable
  public static MinimumTranslationVector checkCollision(Vector2D[] shape, Vector2D circleCenter, float radius) {
    float smallestOverlap = Float.MAX_VALUE;
    Vector2D smallestAxis = null;
    Vector2D[] axes = new Vector2D[shape.length + 1];

    System.arraycopy(getAxes(shape), 0, axes, 0, shape.length);
    axes[shape.length] = getClosestPointOnShapeAxis(circleCenter, shape);

    for (Vector2D axis : axes) {
      Projection p1 = projectShapeToAxis(shape, axis);
      Projection p2 = projectCircleToAxis(circleCenter, radius, axis);

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

    Vector2D centroidA = Shape.calculateCentroid(shape);
    Vector2D centroidB = circleCenter;

    if (centroidB.subtract(centroidA).dot(smallestAxis) < 0) {
      smallestAxis = smallestAxis.scalar(-1);
    }

    return new MinimumTranslationVector(smallestAxis, smallestOverlap);
  }

  private static Projection projectCircleToAxis(Vector2D circleCenter, float radius, Vector2D axis) {
    Vector2D directionAndRadius = axis.scalar(radius);

    Vector2D p1 = circleCenter.add(directionAndRadius);
    Vector2D p2 = circleCenter.subtract(directionAndRadius);

    float min = p1.dot(axis);
    float max = p2.dot(axis);

    if (min > max) {
      float temp = min;

      min = max;
      max = temp;
    }

    return new Projection(min, max);
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

  private static Vector2D getClosestPointOnShapeAxis(Vector2D circleCenter, Vector2D[] shape) {
    Vector2D closestPoint = null;
    float minDistance = Float.MAX_VALUE;

    for (Vector2D point : shape) {
      float distance = point.distanceSq(circleCenter);

      if (distance < minDistance) {
        minDistance = distance;
        closestPoint = point;
      }
    }

    return closestPoint.subtract(circleCenter).normalize();
  }

  private record Projection(float min, float max) {
    public boolean isOverlapping(Projection projection) {
      return projection.max >= min && max >= projection.min;
    }

    public float getOverlap(Projection projection) {
      float overlap = Math.max(0, Math.min(max, projection.max) - Math.max(min, projection.min));

      if (contains(projection) || projection.contains(this)) {
        float minS = Math.abs(this.min - projection.min);
        float maxS = Math.abs(this.max - projection.max);

        overlap += Math.min(minS, maxS);
      }

      return overlap;
    }

    private boolean contains(Projection projection) {
      return min <= projection.min && projection.min <= max && min <= projection.max && projection.max <= max;
    }
  }
}
