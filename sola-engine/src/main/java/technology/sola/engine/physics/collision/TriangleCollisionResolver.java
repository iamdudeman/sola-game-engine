package technology.sola.engine.physics.collision;

import technology.sola.engine.physics.MinimumTranslationVector;
import technology.sola.engine.physics.utils.SeparatingAxisTheorem;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.ConvexPolygon;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Triangle;

public record TriangleCollisionResolver() implements CollisionResolver<Triangle> {
  @Override
  public MinimumTranslationVector resolve(Triangle shape, Rectangle rectangle) {
    return SeparatingAxisTheorem.checkCollision(
      shape.points(), rectangle.points()
    );
  }

  @Override
  public MinimumTranslationVector resolve(Triangle shape, Circle circle) {
    return SeparatingAxisTheorem.checkCollision(shape.points(), circle.center(), circle.radius());
  }

  @Override
  public MinimumTranslationVector resolve(Triangle shape, Triangle triangle) {
    return SeparatingAxisTheorem.checkCollision(
      shape.points(), triangle.points()
    );
  }

  @Override
  public MinimumTranslationVector resolve(Triangle shape, ConvexPolygon convexPolygon) {
    return SeparatingAxisTheorem.checkCollision(
      shape.points(), convexPolygon.points()
    );
  }
}
