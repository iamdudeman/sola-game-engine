package technology.sola.math.geometry;

import org.junit.jupiter.api.Test;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LineTest {
  @Test
  void closestPointOnLine() {
    var line = new Line(new Vector2D(0, 0), new Vector2D(10, 0));

    assertEquals(new Vector2D(5, 0), line.closestPointOnLine(new Vector2D(5, 10)));
    assertEquals(new Vector2D(5, 0), line.closestPointOnLine(new Vector2D(5, -10)));
    assertEquals(new Vector2D(10, 0), line.closestPointOnLine(new Vector2D(20, 20)));
    assertEquals(new Vector2D(0, 0), line.closestPointOnLine(new Vector2D(-20, -20)));
  }
}
