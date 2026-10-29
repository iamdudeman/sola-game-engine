package technology.sola.engine.physics.collision;

import technology.sola.engine.physics.MinimumTranslationVector;
import technology.sola.math.geometry.*;

/**
 * Visitor interface for resolving collision checks between a source {@link Shape} and supported target shape types.
 *
 * @param <S> the source shape type being tested for collision
 */
public interface CollisionVisitor<S extends Shape> {
  /**
   * Visits an axis-aligned bounding box collision target.
   *
   * @param shape     the source shape being tested
   * @param rectangle the rectangle collision target
   * @return the minimum translation vector required to resolve the collision, or {@code null} if there is no collision
   */
  MinimumTranslationVector visitAABB(S shape, Rectangle rectangle);

  /**
   * Visits a circle collision target.
   *
   * @param shape  the source shape being tested
   * @param circle the circle collision target
   * @return the minimum translation vector required to resolve the collision, or {@code null} if there is no collision
   */
  MinimumTranslationVector visitCircle(S shape, Circle circle);

  /**
   * Visits a triangle collision target.
   *
   * @param shape    the source shape being tested
   * @param triangle the triangle collision target
   * @return the minimum translation vector required to resolve the collision, or {@code null} if there is no collision
   */
  MinimumTranslationVector visitTriangle(S shape, Triangle triangle);

  /**
   * Visits a convex polygon collision target.
   *
   * @param shape         the source shape being tested
   * @param convexPolygon the convex polygon collision target
   * @return the minimum translation vector required to resolve the collision, or {@code null} if there is no collision
   */
  MinimumTranslationVector visitConvexPolygon(S shape, ConvexPolygon convexPolygon);
}
