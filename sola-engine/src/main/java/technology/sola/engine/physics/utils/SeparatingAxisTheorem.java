package technology.sola.engine.physics.utils;

import technology.sola.math.geometry.Circle;
import technology.sola.math.linear.Vector2D;

/**
 * SeparatingAxisTheorem contains methods for checking whether two convex polygons are colliding or not.
 */
public class SeparatingAxisTheorem {
  // todo return normal + penetration or null
  public static CollisionInfo checkCollision(Vector2D[] shapeA, Vector2D[] shapeB) {
    Vector2D[] axesA = getAxes(shapeA);
    Vector2D[] axesB = getAxes(shapeB);

    for (int i = 0; i < axesA.length; i++) {
      Vector2D axis = axesA[i];
      Projection p1 = projectShapeToAxis(shapeA, axis);
      Projection p2 = projectShapeToAxis(shapeB, axis);

      if (!p1.isOverlapping(p2)) {
        return null;
      }
    }

    for (int i = 0; i < axesB.length; i++) {
      Vector2D axis = axesB[i];
      Projection p1 = projectShapeToAxis(shapeA, axis);
      Projection p2 = projectShapeToAxis(shapeB, axis);

      if (!p1.isOverlapping(p2)) {
        return null;
      }
    }

    // todo real normal and penetration
    return new CollisionInfo(new Vector2D(0, 0), 0);
  }

  // todo return normal + penetration or null
  public static CollisionInfo checkCollision(Vector2D[] shape, Circle circle) {
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

      // todo might need to normalize so we can get collision info later
      axes[i] = normal;
    }

    return axes;
  }

  public record CollisionInfo(Vector2D normal, float penetration) {
  }

  private record Projection(float min, float max) {
    boolean isOverlapping(Projection projection) {
      return projection.min <= max && max <= projection.max;
    }
  }
}
