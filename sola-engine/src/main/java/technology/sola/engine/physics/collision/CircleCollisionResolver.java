package technology.sola.engine.physics.collision;

import technology.sola.engine.physics.MinimumTranslationVector;
import technology.sola.engine.physics.utils.SeparatingAxisTheorem;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.ConvexPolygon;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

public record CircleCollisionResolver() implements CollisionResolver<Circle> {
  @Override
  public MinimumTranslationVector resolve(Circle shape, Rectangle rectangle) {
    var mtv = CircleRectangleCollisionUtils.calculateAABBVsCircle(rectangle, shape);

    if (mtv == null) {
      return null;
    }

    return mtv.invert();
  }

  @Override
  public MinimumTranslationVector resolve(Circle shape, Circle circle) {
    Vector2D posA = shape.center();
    Vector2D posB = circle.center();
    float distance = posA.distance(posB);
    float penetration = shape.radius() + circle.radius() - distance;

    if (penetration <= 0) {
      return null;
    }

    Vector2D normal = posB.subtract(posA).normalize();

    return new MinimumTranslationVector(normal, penetration);
  }

  @Override
  public MinimumTranslationVector resolve(Circle shape, Triangle triangle) {
    var mtv = SeparatingAxisTheorem.checkCollision(triangle.points(), shape.center(), shape.radius());

    if (mtv == null) {
      return null;
    }

    return mtv.invert();
  }

  @Override
  public MinimumTranslationVector resolve(Circle shape, ConvexPolygon convexPolygon) {
    var mtv = SeparatingAxisTheorem.checkCollision(convexPolygon.points(), shape.center(), shape.radius());

    if (mtv == null) {
      return null;
    }

    return mtv.invert();
  }
}
