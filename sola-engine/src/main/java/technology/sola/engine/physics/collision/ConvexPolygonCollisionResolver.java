package technology.sola.engine.physics.collision;

import technology.sola.engine.physics.MinimumTranslationVector;
import technology.sola.engine.physics.utils.SeparatingAxisTheorem;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.ConvexPolygon;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Triangle;

public record ConvexPolygonCollisionResolver() implements CollisionResolver<ConvexPolygon> {
  @Override
  public MinimumTranslationVector resolve(ConvexPolygon shape, Rectangle rectangle) {
    return SeparatingAxisTheorem.checkCollision(
      shape.points(), rectangle.points()
    );
  }

  @Override
  public MinimumTranslationVector resolve(ConvexPolygon shape, Circle circle) {
    return SeparatingAxisTheorem.checkCollision(shape.points(), circle.center(), circle.radius());
  }

  @Override
  public MinimumTranslationVector resolve(ConvexPolygon shape, Triangle triangle) {
    return SeparatingAxisTheorem.checkCollision(
      shape.points(), triangle.points()
    );
  }

  @Override
  public MinimumTranslationVector resolve(ConvexPolygon shape, ConvexPolygon convexPolygon) {
    return SeparatingAxisTheorem.checkCollision(
      shape.points(), convexPolygon.points()
    );
  }
}
