package technology.sola.math.geometry;

import org.junit.jupiter.api.Test;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConvexPolygonTest {
  @Test
  void contains() {
    Rectangle rectangle = new Rectangle(new Vector2D(0, 0), new Vector2D(10, 10));
    ConvexPolygon convexPolygon = new ConvexPolygon(rectangle.points());

    assertEquals(rectangle.contains(new Vector2D(5, 5)), convexPolygon.contains(new Vector2D(5, 5)));
    assertEquals(rectangle.contains(new Vector2D(20, 20)), convexPolygon.contains(new Vector2D(20, 20)));

    Triangle triangle = new Triangle(new Vector2D(0, 0), new Vector2D(10, 10), new Vector2D(2, 5));
    convexPolygon = new ConvexPolygon(triangle.points());

    assertEquals(triangle.contains(new Vector2D(1, 1)), convexPolygon.contains(new Vector2D(1, 1)));
    assertEquals(triangle.contains(new Vector2D(20, 20)), convexPolygon.contains(new Vector2D(20, 20)));
  }

  @Test
  void area() {
    Rectangle rectangle = new Rectangle(new Vector2D(0, 0), new Vector2D(10, 10));
    ConvexPolygon convexPolygon = new ConvexPolygon(rectangle.points());

    assertEquals(rectangle.getArea(), convexPolygon.getArea());

    Triangle triangle = new Triangle(new Vector2D(0, 0), new Vector2D(10, 10), new Vector2D(2, 5));
    convexPolygon = new ConvexPolygon(triangle.points());

    assertEquals(triangle.getArea(), convexPolygon.getArea());
  }
}
