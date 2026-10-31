package technology.sola.math.geometry;

public interface ShapeVisitor<R> {
  R visit(Rectangle rectangle);

  R visit(Circle circle);

  R visit(Ellipse ellipse);

  R visit(Triangle triangle);

  R visit(ConvexPolygon convexPolygon);
}
