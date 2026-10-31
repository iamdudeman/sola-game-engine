package technology.sola.engine.physics.collision;

import org.jspecify.annotations.Nullable;
import technology.sola.engine.physics.MinimumTranslationVector;
import technology.sola.math.SolaMath;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

class CircleRectangleCollisionUtils {
  /**
   * Calculates a {@link MinimumTranslationVector} for a collision between an axis aligned {@link Rectangle} and
   * {@link Circle}.
   *
   * @param rectangle the rectangle
   * @param circle the circle
   * @return the {@link MinimumTranslationVector} if there is a collision or else null
   */
  @Nullable
  static MinimumTranslationVector calculateAABBVsCircle(Rectangle rectangle, Circle circle) {
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

  private CircleRectangleCollisionUtils() {
  }
}
