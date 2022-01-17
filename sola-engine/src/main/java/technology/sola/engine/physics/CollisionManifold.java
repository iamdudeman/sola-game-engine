package technology.sola.engine.physics;

import technology.sola.engine.ecs.Entity;
import technology.sola.math.linear.Vector2D;

import java.util.Objects;

public record CollisionManifold(Entity entityA, Entity entityB,
                                Vector2D normal, float penetration) {
  /**
   * Creates a CollisionManifoldEventMessage for two entities.
   *
   * @param entityA     the first {@link Entity} in the collision
   * @param entityB     the second {@link Entity} in the collision
   * @param normal      the collision normal
   * @param penetration the penetration of the collision
   */
  public CollisionManifold {
  }

  /**
   * Checks if this instance is equal to another object. It will return true regardless of the order of entityA and entityB.
   * This assumes that there can only be one collision between any two {@link Entity} at the same time.
   * <p>
   * Example: If this manifold has entityA with id 1 and entityB with id 2 and another manifold has entityA
   * with id 2 and entityB with id 1 it will return true.
   *
   * @param o Object to check equality with
   * @return true if equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CollisionManifold that = (CollisionManifold) o;

    return
      (entityA.equals(that.entityA) && entityB.equals(that.entityB)) ||
        (entityA.equals(that.entityB) && entityB.equals(that.entityA));
  }

  @Override
  public int hashCode() {
    int min = Math.min(entityA.getIndexInWorld(), entityB.getIndexInWorld());
    int max = Math.max(entityA.getIndexInWorld(), entityB.getIndexInWorld());

    return Objects.hash(min, max);
  }
}
