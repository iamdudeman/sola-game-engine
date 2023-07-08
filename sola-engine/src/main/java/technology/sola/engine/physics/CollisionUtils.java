package technology.sola.engine.physics;

import technology.sola.ecs.Entity;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.math.SolaMath;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

/**
 * The CollisionUtils class is a collection of various methods that help in collision detection and resolution.
 */
public final class CollisionUtils {
  /**
   * Calculates a {@link CollisionManifold} for two {@link Entity} that collided.
   *
   * @param viewEntryA the first Entity in a {@link technology.sola.ecs.view.View2Entry} of {@link ColliderComponent} and {@link TransformComponent}
   * @param viewEntryB the second Entity in a {@link technology.sola.ecs.view.View2Entry} of {@link ColliderComponent} and {@link TransformComponent}
   * @return the resulting {@code CollisionManifold}
   */
  public static CollisionManifold calculateCollisionManifold(
    View2Entry<ColliderComponent, TransformComponent> viewEntryA,
    View2Entry<ColliderComponent, TransformComponent> viewEntryB
  ) {
    Entity entityA = viewEntryA.entity();
    Entity entityB = viewEntryB.entity();
    TransformComponent transformA = viewEntryA.c2();
    TransformComponent transformB = viewEntryB.c2();
    ColliderComponent colliderA = viewEntryA.c1();
    ColliderComponent colliderB = viewEntryB.c1();

    return switch (colliderA.getColliderType()) {
      case AABB -> switch (colliderB.getColliderType()) {
        case AABB ->
          calculateAABBVsAABB(entityA, entityB, colliderA.asRectangle(transformA), colliderB.asRectangle(transformB));
        case CIRCLE ->
          calculateAABBVsCircle(entityA, entityB, colliderA.asRectangle(transformA), colliderB.asCircle(transformB));
      };
      case CIRCLE -> switch (colliderB.getColliderType()) {
        case AABB ->
          calculateAABBVsCircle(entityB, entityA, colliderB.asRectangle(transformB), colliderA.asCircle(transformA));
        case CIRCLE ->
          calculateCircleVsCircle(entityA, entityB, colliderA.asCircle(transformA), colliderB.asCircle(transformB));
      };
    };
  }

  private static CollisionManifold calculateAABBVsCircle(
    Entity rectEntity, Entity circeEntity,
    Rectangle rectangle, Circle circle
  ) {
    Vector2D circleCenter = circle.center();
    Vector2D closestPointOnRectangle = SolaMath.clamp(rectangle.min(), rectangle.max(), circleCenter);
    boolean isCircleCenterInsideRectangle = circleCenter.equals(closestPointOnRectangle);

    if (isCircleCenterInsideRectangle) {
      // Find the closest edge since that is the closest point on rectangle for the normal
      float minDistanceX = circleCenter.x() - rectangle.min().x();
      float maxDistanceX = rectangle.max().x() - circleCenter.x();
      float minDistanceY = circleCenter.y() - rectangle.min().y();
      float maxDistanceY = rectangle.max().y() - circleCenter.y();

      if (maxDistanceY < minDistanceY && maxDistanceY < maxDistanceX && maxDistanceY < minDistanceX) {
        closestPointOnRectangle = new Vector2D(circleCenter.x(), rectangle.max().y());
      } else if (minDistanceY < maxDistanceX && minDistanceY < minDistanceX) {
        closestPointOnRectangle = new Vector2D(circleCenter.x(), rectangle.min().y());
      } else if (maxDistanceX < minDistanceX) {
        closestPointOnRectangle = new Vector2D(rectangle.max().x(), circleCenter.y());
      } else {
        closestPointOnRectangle = new Vector2D(rectangle.min().x(), circleCenter.y());
      }
    }

    // Normal
    Vector2D diff = closestPointOnRectangle.subtract(circleCenter);

    if (!isCircleCenterInsideRectangle && diff.magnitudeSq() > circle.radius() * circle.radius()) {
      return null;
    }

    float penetration = circle.radius() - closestPointOnRectangle.distance(circleCenter);
    Vector2D normal = diff.normalize();

    // If not inside
    if (!isCircleCenterInsideRectangle) {
      normal = normal.scalar(-1);
    }

    return new CollisionManifold(rectEntity, circeEntity, normal, penetration);
  }

  private static CollisionManifold calculateAABBVsAABB(
    Entity entityA, Entity entityB,
    Rectangle rectangleA, Rectangle rectangleB
  ) {
    Vector2D aBoxMin = rectangleA.min();
    Vector2D aBoxMax = rectangleA.max();
    Vector2D bBoxMin = rectangleB.min();
    Vector2D bBoxMax = rectangleB.max();

    Vector2D posDiffMin = bBoxMin.subtract(aBoxMin);
    Vector2D posDiffMax = bBoxMax.subtract(aBoxMax);

    float aWidth = aBoxMax.x() - aBoxMin.x();
    float bWidth = bBoxMax.x() - bBoxMin.x();
    float xAxisOverlap = (aWidth + bWidth - (Math.abs(posDiffMin.x()) + Math.abs(posDiffMax.x()))) / 2;

    // No collision if no x overlap
    if (xAxisOverlap <= 0) {
      return null;
    }

    float aHeight = aBoxMax.y() - aBoxMin.y();
    float bHeight = bBoxMax.y() - bBoxMin.y();
    float yAxisOverlap = (aHeight + bHeight - (Math.abs(posDiffMin.y()) + Math.abs(posDiffMax.y()))) / 2;

    // No collision if no y overlap
    if (yAxisOverlap <= 0) {
      return null;
    }

    // Collision found, calculate normal and penetration
    final Vector2D normal;
    final float penetration;

    if (xAxisOverlap < yAxisOverlap) {
      penetration = xAxisOverlap;
      if (posDiffMin.x() > 0)
        normal = new Vector2D(1, 0);
      else
        normal = new Vector2D(-1, 0);
    } else {
      penetration = yAxisOverlap;
      if (posDiffMin.y() > 0)
        normal = new Vector2D(0, 1);
      else
        normal = new Vector2D(0, -1);
    }

    return new CollisionManifold(entityA, entityB, normal, penetration);
  }

  private static CollisionManifold calculateCircleVsCircle(
    Entity entityA, Entity entityB,
    Circle circleA, Circle circleB
  ) {
    Vector2D posA = circleA.center();
    Vector2D posB = circleB.center();
    float distance = posA.distance(posB);
    float penetration = circleA.radius() + circleB.radius() - distance;

    if (penetration <= 0) {
      return null;
    }

    Vector2D normal = posB.subtract(posA).normalize();

    return new CollisionManifold(entityA, entityB, normal, penetration);
  }

  private CollisionUtils() {
  }
}

