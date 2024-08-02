package technology.sola.engine.physics.utils;

import technology.sola.ecs.Entity;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.math.SolaMath;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Triangle;
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

    return switch (colliderA.getType()) {
      case AABB -> switch (colliderB.getType()) {
        case AABB -> calculateAABBVsAABB(
          entityA, entityB, colliderA.getShape(transformA), colliderB.getShape(transformB)
        );
        case CIRCLE -> calculateAABBVsCircle(
          entityA, entityB, colliderA.getShape(transformA), colliderB.getShape(transformB)
        );
        case TRIANGLE -> calculateAABBVsTriangle(
          entityA, entityB, colliderA.getShape(transformA), colliderB.getShape(transformB)
        );
      };
      case CIRCLE -> switch (colliderB.getType()) {
        case AABB -> calculateAABBVsCircle(
          entityB, entityA, colliderB.getShape(transformB), colliderA.getShape(transformA)
        );
        case CIRCLE -> calculateCircleVsCircle(
          entityA, entityB, colliderA.getShape(transformA), colliderB.getShape(transformB)
        );
        case TRIANGLE -> calculateCircleVsTriangle(
          entityA, entityB, colliderA.getShape(transformA), colliderB.getShape(transformB)
        );
      };
      case TRIANGLE -> switch (colliderB.getType()) {
        case AABB -> calculateAABBVsTriangle(
          entityB, entityA, colliderB.getShape(transformB), colliderA.getShape(transformA)
        );
        case CIRCLE -> calculateCircleVsTriangle(
          entityB, entityA, colliderB.getShape(transformB), colliderA.getShape(transformA)
        );
        case TRIANGLE -> calculateTriangleVsTriangle(
          entityA, entityB, colliderA.getShape(transformA), colliderB.getShape(transformB)
        );
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

  private static CollisionManifold calculateAABBVsTriangle(
    Entity entityA, Entity entityB,
    Rectangle rectangle, Triangle triangle
  ) {
    var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(rectangle.getPoints(), triangle.getPoints());

    if (minimumTranslationVector == null) {
      return null;
    }

    return new CollisionManifold(entityA, entityB, minimumTranslationVector.normal(), minimumTranslationVector.penetration());
  }

  private static CollisionManifold calculateCircleVsTriangle(
    Entity entityA, Entity entityB,
    Circle circle, Triangle triangle
  ) {

    // todo consider https://www.phatcode.net/articles.php?id=459
    //  vertex test (any triangle point in circle)
    //  circle within triangle test
    //  edge test (what is partially implemented below)

    /*
    todo also consider
    First, calculate the lengths between each point on the triangle to the circle's centre.
    Second, find which length is shortest.
    Third, if this length is greater than the radius of the circle, there is no collision.
     */

    // todo also consider SAT https://www.codezealot.org/archives/55/

    var circleCenter = circle.center();
    var triangleCenter = triangle.getCentroid();

    int intersectionEdge = 0;
    var intersection = calculateLineLineIntersection(circleCenter, triangleCenter, triangle.p1(), triangle.p2());

    if (intersection == null) {
      intersection = calculateLineLineIntersection(circleCenter, triangleCenter, triangle.p2(), triangle.p3());
      intersectionEdge = 1;
    }

    if (intersection == null) {
      intersection = calculateLineLineIntersection(circleCenter, triangleCenter, triangle.p3(), triangle.p1());
      intersectionEdge = 2;
    }

    if (intersection == null) {
      return null;
    }

    Vector2D normal = new Vector2D(0, 0);

    if (intersectionEdge == 0) {
      normal = new Vector2D(-triangle.p2().x() + triangle.p1().x(), triangle.p2().y() - triangle.p1().y()).normalize();
    } else if (intersectionEdge == 1) {
      normal = new Vector2D(-triangle.p3().x() + triangle.p2().x(), triangle.p3().y() - triangle.p2().y()).normalize();
    } else if (intersectionEdge == 2) {
      normal = new Vector2D(-triangle.p1().x() + triangle.p3().x(), triangle.p1().y() - triangle.p3().y()).normalize();
    }

    System.out.println(intersectionEdge + " " + normal + " " + (circleCenter.distance(triangleCenter) - intersection.distance(circleCenter)));

    // normal is negative reciprocal

    // todo implement
//    throw new RuntimeException("not yet implemented");
    return new CollisionManifold(
      entityA, entityB, normal.scalar(-1), (circleCenter.distance(triangleCenter) - intersection.distance(circleCenter)) / 2
    );
  }

  private static CollisionManifold calculateTriangleVsTriangle(
    Entity entityA, Entity entityB,
    Triangle triangleA, Triangle triangleB
  ) {
    // todo implement
    throw new RuntimeException("not yet implemented");
  }

  private static Vector2D calculateLineLineIntersection(Vector2D start1, Vector2D end1, Vector2D start2, Vector2D end2) {
    float a1 = end1.y() - start1.y();
    float b1 = start1.x() - end1.x();
    float c1 = a1 * start1.x() + b1 * start1.y();

    float a2 = end2.y() - start2.y();
    float b2 = start2.x() - end2.x();
    float c2 = a2 * start2.x() + b2 * start2.y();

    float determinant = a1 * b2 - a2 * b1;

    if (Float.compare(determinant, 0.0f) == 0) {
      return null;
    }

    float x = (b2 * c1 - b1 * c2) / determinant;
    float y = (a1 * c2 - a2 * c1) / determinant;

    return new Vector2D(x, y);
  }

  private CollisionUtils() {
  }
}
