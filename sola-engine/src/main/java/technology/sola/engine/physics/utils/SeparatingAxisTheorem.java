package technology.sola.engine.physics.utils;

import technology.sola.math.geometry.Circle;
import technology.sola.math.linear.Vector2D;

/**
 * SeparatingAxisTheorem contains methods for checking whether two convex polygons are colliding or not.
 */
public class SeparatingAxisTheorem {
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

    return new MinimumTranslationVector(smallestAxis, smallestOverlap);
  }

  // todo return normal + penetration or null
  public static MinimumTranslationVector checkCollision(Vector2D[] shape, Circle circle) {
    // todo implement

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
