package technology.sola.engine.physics.utils;

import org.jspecify.annotations.Nullable;
import technology.sola.engine.physics.MinimumTranslationVector;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Shape;

class MinimumTranslationVectorUtils {
  @Nullable
  static MinimumTranslationVector calculateMinimumTranslationVector(Shape shapeA, Shape shapeB) {
    if (shapeA instanceof Rectangle rectangleA) {
      return calculateForRectangle(rectangleA, shapeB);
    }

    if (shapeA instanceof Circle circleA) {
      return calculateForCircle(circleA, shapeB);
    }

    return calculateForShape(shapeA, shapeB);
  }

  private static MinimumTranslationVector calculateForRectangle(Rectangle rectangleA, Shape shapeB) {
    if (shapeB instanceof Rectangle rectangleB) {
      return CollisionUtils.calculateAABBVsAABB(rectangleA, rectangleB);
    }

    if (shapeB instanceof Circle circleB) {
      return CollisionUtils.calculateAABBVsCircle(rectangleA, circleB);
    }

    var pointsB = shapeB.points();

    if (pointsB.length > 0) {
      return SeparatingAxisTheorem.checkCollision(rectangleA.points(), pointsB);
    }

    throw new UnsupportedCollisionException(rectangleA, shapeB);
  }

  private static MinimumTranslationVector calculateForCircle(Circle circleA, Shape shapeB) {
    if (shapeB instanceof Rectangle rectangleB) {
      return reverseMtv(CollisionUtils.calculateAABBVsCircle(rectangleB, circleA));
    }

    if (shapeB instanceof Circle circleB) {
      return CollisionUtils.calculateCircleVsCircle(circleA, circleB);
    }

    var pointsB = shapeB.points();

    if (pointsB.length > 0) {
      return reverseMtv(SeparatingAxisTheorem.checkCollision(pointsB, circleA.center(), circleA.radius()));
    }

    throw new UnsupportedCollisionException(circleA, shapeB);
  }

  private static MinimumTranslationVector calculateForShape(Shape shapeA, Shape shapeB) {
    var pointsA = shapeA.points();

    if (shapeB instanceof Circle circleB) {
      return SeparatingAxisTheorem.checkCollision(pointsA, circleB.center(), circleB.radius());
    }

    var pointsB = shapeB.points();

    if (pointsA.length > 0 && pointsB.length > 0) {
      return SeparatingAxisTheorem.checkCollision(pointsA, pointsB);
    }

    throw new UnsupportedCollisionException(shapeA, shapeB);
  }

  @Nullable
  private static MinimumTranslationVector reverseMtv(@Nullable MinimumTranslationVector minimumTranslationVector) {
    if (minimumTranslationVector == null) {
      return null;
    }

    return minimumTranslationVector.reverse();
  }

  static class UnsupportedCollisionException extends UnsupportedOperationException {
    UnsupportedCollisionException(Shape shapeA, Shape shapeB) {
      super(String.format(
        "Unsupported Shape collision combination [%s, %s]",
        shapeA.getClass().getSimpleName(),
        shapeB.getClass().getSimpleName()
      ));
    }
  }
}
