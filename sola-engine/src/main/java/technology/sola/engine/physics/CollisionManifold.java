package technology.sola.engine.physics;

import technology.sola.ecs.Entity;
import technology.sola.math.linear.Vector2D;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public record CollisionManifold(
  Entity entityA, Entity entityB, Vector2D normal, float penetration
) {
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

  public void conditionallyResolveCollision(
    Function<Entity, Boolean> entityOneCollisionCondition,
    Function<Entity, Boolean> entityTwoCollisionCondition,
    BiConsumer<Entity, Entity> collisionResolver
  ) {
    if (entityOneCollisionCondition.apply(entityA) && entityTwoCollisionCondition.apply(entityB)) {
      collisionResolver.accept(entityA, entityB);
    } else if (entityOneCollisionCondition.apply(entityB) && entityTwoCollisionCondition.apply(entityA)) {
      collisionResolver.accept(entityB, entityA);
    }
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

    if (o instanceof CollisionManifold that) {
      return (entityA == that.entityA && entityB == that.entityB) || (entityA == that.entityB && entityB == that.entityA);
    }

    return false;
  }

  @Override
  public int hashCode() {
    int min = Math.min(entityA.getIndexInWorld(), entityB.getIndexInWorld());
    int max = Math.max(entityA.getIndexInWorld(), entityB.getIndexInWorld());

    return Objects.hash(min, max);
  }
}
