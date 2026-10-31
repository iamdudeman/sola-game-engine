package technology.sola.engine.physics.collision;

import technology.sola.engine.physics.MinimumTranslationVector;
import technology.sola.math.geometry.*;

/**
 * Visitor interface for resolving collision checks between a source {@link Shape} and supported target shape types.
 *
 * @param <S> the source shape type being tested for collision
 */
public interface CollisionResolver<S extends Shape> {
  default MinimumTranslationVector resolve(S shape, Shape otherShape) {
    if (otherShape instanceof Rectangle rectangle) {
      return resolve(shape, rectangle);
    }

    if (otherShape instanceof Circle circle) {
      return resolve(shape, circle);
    }

    if (otherShape instanceof Triangle triangle) {
      return resolve(shape, triangle);
    }

    if (otherShape instanceof ConvexPolygon convexPolygon) {
      return resolve(shape, convexPolygon);
    }

    throw new IllegalArgumentException("Unsupported shape type: " + otherShape.getClass().getName());
  }

  /**
   * Visits an axis-aligned bounding box collision target.
   *
   * @param shape     the source shape being tested
   * @param rectangle the rectangle collision target
   * @return the minimum translation vector required to resolve the collision, or {@code null} if there is no collision
   */
  MinimumTranslationVector resolve(S shape, Rectangle rectangle);

  /**
   * Visits a circle collision target.
   *
   * @param shape  the source shape being tested
   * @param circle the circle collision target
   * @return the minimum translation vector required to resolve the collision, or {@code null} if there is no collision
   */
  MinimumTranslationVector resolve(S shape, Circle circle);

  /**
   * Visits a triangle collision target.
   *
   * @param shape    the source shape being tested
   * @param triangle the triangle collision target
   * @return the minimum translation vector required to resolve the collision, or {@code null} if there is no collision
   */
  MinimumTranslationVector resolve(S shape, Triangle triangle);

  /**
   * Visits a convex polygon collision target.
   *
   * @param shape         the source shape being tested
   * @param convexPolygon the convex polygon collision target
   * @return the minimum translation vector required to resolve the collision, or {@code null} if there is no collision
   */
  MinimumTranslationVector resolve(S shape, ConvexPolygon convexPolygon);
}
