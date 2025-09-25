package technology.sola.engine.physics.utils;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Entity;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.MinimumTranslationVector;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.math.SolaMath;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Shape;
import technology.sola.math.linear.Vector2D;

/**
 * The CollisionUtils class is a collection of various methods that help in collision detection and resolution.
 */
@NullMarked
public final class CollisionUtils {
  /**
   * Calculates a {@link CollisionManifold} for two {@link Entity} that collided.
   *
   * @param viewEntryA the first Entity in a {@link technology.sola.ecs.view.View2Entry} of {@link ColliderComponent} and {@link TransformComponent}
   * @param viewEntryB the second Entity in a {@link technology.sola.ecs.view.View2Entry} of {@link ColliderComponent} and {@link TransformComponent}
   * @return the resulting {@code CollisionManifold}
   */
  @Nullable
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
    boolean isReversed = false;

    var mtv = switch (colliderA.getType()) {
      case AABB -> switch (colliderB.getType()) {
        case AABB -> calculateAABBVsAABB(
          colliderA.getShape(transformA), colliderB.getShape(transformB)
        );
        case TRIANGLE -> SeparatingAxisTheorem.checkCollision(
          colliderA.getShape(transformA).getPoints(), colliderB.getShape(transformB).getPoints()
        );
        case CIRCLE -> calculateAABBVsCircle(
          colliderA.getShape(transformA), colliderB.getShape(transformB)
        );
      };
      case CIRCLE -> switch (colliderB.getType()) {
        case AABB -> {
          isReversed = true;
          yield calculateAABBVsCircle(
            colliderB.getShape(transformB), colliderA.getShape(transformA)
          );
        }
        case CIRCLE -> calculateCircleVsCircle(
          colliderA.getShape(transformA), colliderB.getShape(transformB)
        );
        case TRIANGLE -> {
          isReversed = true;
          yield calculateCircleVsShapeSAT(
            colliderA.getShape(transformA), colliderB.getShape(transformB)
          );
        }
      };
      case TRIANGLE -> switch (colliderB.getType()) {
        case AABB, TRIANGLE -> SeparatingAxisTheorem.checkCollision(
          colliderA.getShape(transformA).getPoints(), colliderB.getShape(transformB).getPoints()
        );
        case CIRCLE -> calculateCircleVsShapeSAT(
          colliderB.getShape(transformB), colliderA.getShape(transformA)
        );
      };
    };

    if (mtv == null) {
      return null;
    }

    if (isReversed) {
      var temp = entityA;

      entityA = entityB;
      entityB = temp;
    }

    return new CollisionManifold(entityA, entityB, mtv.normal(), mtv.penetration());
  }

  /**
   * Calculates a {@link MinimumTranslationVector} for a collision between an axis aligned {@link Rectangle} and
   * {@link Circle}.
   *
   * @param rectangle the rectangle
   * @param circle the circle
   * @return the {@link MinimumTranslationVector} if there is a collision or else null
   */
  @Nullable
  public static MinimumTranslationVector calculateAABBVsCircle(Rectangle rectangle, Circle circle) {
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

    float distance = closestPointOnRectangle.distance(circleCenter);
    float penetration = circle.radius() - distance;
    Vector2D normal = diff.normalize();

    if (isCircleCenterInsideRectangle) {
      penetration = circle.radius() + distance;
    } else {
      normal = normal.scalar(-1);
    }

    return new MinimumTranslationVector(normal, penetration);
  }

  /**
   * Calculates a {@link MinimumTranslationVector} for a collision between two axis-aligned {@link Rectangle}s.
   *
   * @param rectangleA the first rectangle
   * @param rectangleB the second rectangle
   * @return the {@link MinimumTranslationVector} if there is a collision or else null
   */
  @Nullable
  public static MinimumTranslationVector calculateAABBVsAABB(Rectangle rectangleA, Rectangle rectangleB) {
    var aBoxMin = rectangleA.min();
    var bBoxMin = rectangleB.min();
    var centerA = rectangleA.getCenter();
    var centerB = rectangleB.getCenter();
    var centerDiff = centerB.subtract(centerA);

    float xAxisOverlap = (centerB.x() - bBoxMin.x() + centerA.x() - aBoxMin.x()) - Math.abs(centerDiff.x());

    // No collision if no x overlap
    if (xAxisOverlap <= 0) {
      return null;
    }

    float yAxisOverlap = (centerB.y() - bBoxMin.y() + centerA.y() - aBoxMin.y()) - Math.abs(centerDiff.y());

    // No collision if no y overlap
    if (yAxisOverlap <= 0) {
      return null;
    }

    // Collision found, calculate normal and penetration
    final Vector2D normal;
    float penetration;

    if (xAxisOverlap < yAxisOverlap) {
      penetration = xAxisOverlap;

      if (centerDiff.x() > 0) {
        normal = new Vector2D(1, 0);
      } else {
        normal = new Vector2D(-1, 0);
      }
    } else {
      penetration = yAxisOverlap;

      if (centerDiff.y() > 0) {
        normal = new Vector2D(0, 1);
      } else {
        normal = new Vector2D(0, -1);
      }
    }

    return new MinimumTranslationVector(normal, penetration);
  }

  /**
   * Calculates a {@link MinimumTranslationVector} for a collision between two {@link Circle}s.
   *
   * @param circleA the first circle
   * @param circleB the second circle
   * @return the {@link MinimumTranslationVector} if there is a collision or else null
   */
  @Nullable
  public static MinimumTranslationVector calculateCircleVsCircle(Circle circleA, Circle circleB) {
    Vector2D posA = circleA.center();
    Vector2D posB = circleB.center();
    float distance = posA.distance(posB);
    float penetration = circleA.radius() + circleB.radius() - distance;

    if (penetration <= 0) {
      return null;
    }

    Vector2D normal = posB.subtract(posA).normalize();

    return new MinimumTranslationVector(normal, penetration);
  }

  @Nullable
  private static MinimumTranslationVector calculateCircleVsShapeSAT(Circle circle, Shape shape) {
    return SeparatingAxisTheorem.checkCollision(shape.getPoints(), circle.center(), circle.radius());
  }

  private CollisionUtils() {
  }
}
