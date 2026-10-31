package technology.sola.engine.physics.collision;

import technology.sola.math.geometry.*;

public class CollisionResolverVisitor<T extends Shape> implements ShapeVisitor<CollisionResolver<T>> {
  private final CollisionResolver rectangleCollisionResolver = new RectangleCollisionResolver();
  private final CollisionResolver circleCollisionResolver = new CircleCollisionResolver();
  private final CollisionResolver triangleCollisionResolver = new TriangleCollisionResolver();
  private final CollisionResolver convexPolygonCollisionResolver = new ConvexPolygonCollisionResolver();

  @Override
  public CollisionResolver<T> visit(Rectangle rectangle) {
    return rectangleCollisionResolver;
  }

  @Override
  public CollisionResolver<T> visit(Circle circle) {
    return circleCollisionResolver;
  }

  @Override
  public CollisionResolver<T> visit(Ellipse ellipse) {
    throw new UnsupportedOperationException("Collision resolution for ellipse is not implemented yet.");
  }

  @Override
  public CollisionResolver<T> visit(Triangle triangle) {
    return triangleCollisionResolver;
  }

  @Override
  public CollisionResolver<T> visit(ConvexPolygon convexPolygon) {
    return convexPolygonCollisionResolver;
  }
}
