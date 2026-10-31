package technology.sola.engine.physics.collision;

import technology.sola.engine.physics.MinimumTranslationVector;
import technology.sola.engine.physics.utils.SeparatingAxisTheorem;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.ConvexPolygon;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

public record RectangleCollisionResolver() implements CollisionResolver<Rectangle> {
  @Override
  public MinimumTranslationVector resolve(Rectangle shape, Rectangle rectangle) {
    var aBoxMin = shape.min();
    var bBoxMin = rectangle.min();
    var centerA = shape.getCenter();
    var centerB = rectangle.getCenter();
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

  @Override
  public MinimumTranslationVector resolve(Rectangle shape, Circle circle) {
    return CircleRectangleCollisionUtils.calculateAABBVsCircle(shape, circle);
  }

  @Override
  public MinimumTranslationVector resolve(Rectangle shape, Triangle triangle) {
    return SeparatingAxisTheorem.checkCollision(
      shape.points(), triangle.points()
    );
  }

  @Override
  public MinimumTranslationVector resolve(Rectangle shape, ConvexPolygon convexPolygon) {
    return SeparatingAxisTheorem.checkCollision(
      shape.points(), convexPolygon.points()
    );
  }
}
