package technology.sola.math.geometry;

import org.jspecify.annotations.NullMarked;
import technology.sola.math.linear.Vector2D;

@NullMarked
public record ConvexPolygon(Vector2D[] points) implements Shape {
  public ConvexPolygon {
    // todo validate it is a convex polygon
  }

  @Override
  public boolean contains(Vector2D point) {
    return false;
  }

  @Override
  public float getArea() {
    return 0;
  }
}
