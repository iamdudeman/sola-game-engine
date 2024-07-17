package technology.sola.engine.physics.utils;

import technology.sola.math.geometry.Circle;
import technology.sola.math.linear.Vector2D;

public class SeparatingAxisTheorem {
  // todo return normal + penetration or null
  public static CollisionInfo checkCollision(Vector2D[] shapeA, Vector2D[] shapeB) {
    // todo implement

    return null;
  }

  // todo return normal + penetration or null
  public static CollisionInfo checkCollision(Vector2D[] shape, Circle circle) {
    // todo implement

    return null;
  }

  public record CollisionInfo(Vector2D normal, float penetration) {
  }
}
