package technology.sola.engine.physics;

import technology.sola.engine.ecs.Entity;
import technology.sola.math.linear.Vector2D;

import java.util.Objects;

public class CollisionManifold {
  private final Entity entityA;
  private final Entity entityB;
  private final Vector2D normal;
  private final float penetration;

  /**
   * Creates a CollisionManifoldEventMessage for two entities.
   *
   * @param entityA  the first {@link Entity} in the collision
   * @param entityB  the second {@link Entity} in the collision
   * @param normal  the collision normal
   * @param penetration  the penetration of the collision
   */
  public CollisionManifold(Entity entityA, Entity entityB, Vector2D normal, float penetration) {
    this.entityA = entityA;
    this.entityB = entityB;
    this.normal = normal;
    this.penetration = penetration;
  }

  /**
   * Gets the first {@link Entity} for the collision.
   *
   * @return the first {@code Entity} for the collision
   */
  public Entity getEntityA() {
    return entityA;
  }

  /**
   * Gets the second {@link Entity} for the collision.
   *
   * @return the second {@code Entity} for the collision
   */
  public Entity getEntityB() {
    return entityB;
  }

  /**
   * Gets the collision normal.
   *
   * @return the collision normal
   */
  public Vector2D getNormal() {
    return normal;
  }

  /**
   * Gets the collision penetration.
   *
   * @return the collision penetration
   */
  public float getPenetration() {
    return penetration;
  }

  /**
   * Checks if this instance is equal to another object. It will return true regardless of the order of entityA and entityB.
   * This assumes that there can only be one collision between any two {@link Entity} at the same time.
   * <p>
   * Example: If this manifold has entityA with id 1 and entityB with id 2 and another manifold has entityA
   * with id 2 and entityB with id 1 it will return true.
   *
   * @param o  Object to check equality with
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
    int min = Math.min(entityA.getId(), entityB.getId());
    int max = Math.max(entityA.getId(), entityB.getId());

    return Objects.hash(min, max);
  }

}
