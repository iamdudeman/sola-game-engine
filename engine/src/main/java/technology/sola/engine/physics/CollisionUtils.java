package technology.sola.engine.physics;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.math.SolaMath;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

public final class CollisionUtils {
  public static CollisionManifold calculateCollisionManifold(
    Entity entityA, Entity entityB,
    TransformComponent transformA, TransformComponent transformB,
    ColliderComponent colliderA, ColliderComponent colliderB
  ) {
    switch (colliderA.getColliderType()) {
      case AABB:
        switch (colliderB.getColliderType()) {
          case AABB:
            Rectangle rectangleA = colliderA.asRectangle(transformA);
            Rectangle rectangleB = colliderB.asRectangle(transformB);

            return calculateAABBVsAABB(entityA, entityB, rectangleA, rectangleB);
          case CIRCLE:
            Rectangle rectangle = colliderA.asRectangle(transformA);
            Circle circle = colliderB.asCircle(transformB);

            return calculateAABBVsCircle(entityA, entityB, rectangle, circle);
          default:
        }
        break;
      case CIRCLE:
        switch (colliderB.getColliderType()) {
          case AABB:
            Rectangle rectangle = colliderB.asRectangle(transformB);
            Circle circle = colliderA.asCircle(transformA);

            return calculateAABBVsCircle(entityB, entityA, rectangle, circle);
          case CIRCLE:
            Circle circleA = colliderA.asCircle(transformA);
            Circle circleB = colliderB.asCircle(transformB);

            return calculateCircleVsCircle(entityA, entityB, circleA, circleB);
          default:
        }
        break;
      default:
    }

    return null;
  }

  private static CollisionManifold calculateAABBVsCircle(
    Entity rectEntity, Entity circeEntity,
    Rectangle rectangle, Circle circle
  ) {
    Vector2D circleCenter = circle.getCenter();
    Vector2D closestPointOnRectangle = SolaMath.clamp(rectangle.getMin(), rectangle.getMax(), circleCenter);
    boolean isCircleCenterInsideRectangle = circleCenter.equals(closestPointOnRectangle);

    if (isCircleCenterInsideRectangle) {
      // Find the closest edge since that is the closest point on rectangle for the normal
      float minDistanceX = circleCenter.x - rectangle.getMin().x;
      float maxDistanceX = rectangle.getMax().x - circleCenter.x;
      float minDistanceY = circleCenter.y - rectangle.getMin().y;
      float maxDistanceY = rectangle.getMax().y - circleCenter.y;

      if (maxDistanceY < minDistanceY && maxDistanceY < maxDistanceX && maxDistanceY < minDistanceX) {
        closestPointOnRectangle = new Vector2D(circleCenter.x, rectangle.getMax().y);
      } else if (minDistanceY < maxDistanceX && minDistanceY < minDistanceX) {
        closestPointOnRectangle = new Vector2D(circleCenter.x, rectangle.getMin().y);
      } else if (maxDistanceX < minDistanceX) {
        closestPointOnRectangle = new Vector2D(rectangle.getMax().x, circleCenter.y);
      } else {
        closestPointOnRectangle = new Vector2D(rectangle.getMin().x, circleCenter.y);
      }
    }

    // Normal
    Vector2D diff = closestPointOnRectangle.subtract(circleCenter);

    if (diff.magnitudeSq() > circle.getRadius() * circle.getRadius()) {
      return null;
    }

    float penetration = circle.getRadius() - closestPointOnRectangle.distance(circleCenter);
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
    Vector2D aBoxMin = rectangleA.getMin();
    Vector2D aBoxMax = rectangleA.getMax();
    Vector2D bBoxMin = rectangleB.getMin();
    Vector2D bBoxMax = rectangleB.getMax();

    Vector2D posDiffMin = bBoxMin.subtract(aBoxMin);
    Vector2D posDiffMax = bBoxMax.subtract(aBoxMax);

    float aWidth = aBoxMax.x - aBoxMin.x;
    float bWidth = bBoxMax.x - bBoxMin.x;
    float xAxisOverlap = (aWidth + bWidth - (Math.abs(posDiffMin.x) + Math.abs(posDiffMax.x))) / 2;

    // No collision if no x overlap
    if (xAxisOverlap <= 0) {
      return null;
    }

    float aHeight = aBoxMax.y - aBoxMin.y;
    float bHeight = bBoxMax.y - bBoxMin.y;
    float yAxisOverlap = (aHeight + bHeight - (Math.abs(posDiffMin.y) + Math.abs(posDiffMax.y))) / 2;

    // No collision if no y overlap
    if (yAxisOverlap <= 0) {
      return null;
    }

    // Collision found, calculate normal and penetration
    final Vector2D normal;
    final float penetration;

    if (xAxisOverlap < yAxisOverlap) {
      penetration = xAxisOverlap;
      if (posDiffMin.x > 0)
        normal = new Vector2D(1, 0);
      else
        normal = new Vector2D(-1, 0);
    } else {
      penetration = yAxisOverlap;
      if (posDiffMin.y > 0)
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
    Vector2D posA = circleA.getCenter();
    Vector2D posB = circleB.getCenter();
    float distance = posA.distance(posB);
    float penetration = circleA.getRadius() + circleB.getRadius() - distance;

    if (penetration <= 0) {
      return null;
    }

    Vector2D normal = posB.subtract(posA).normalize();

    return new CollisionManifold(entityA, entityB, normal, penetration);
  }

  private CollisionUtils() {
  }
}

