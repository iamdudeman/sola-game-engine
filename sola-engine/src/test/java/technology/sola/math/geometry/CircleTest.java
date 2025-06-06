package technology.sola.math.geometry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CircleTest {
  @Nested
  class constructor {
    @Test
    void when_shouldSetValues() {
      Circle circle = new Circle(5f, new Vector2D(0, 0));

      assertEquals(5f, circle.radius());
      assertEquals(new Vector2D(0, 0), circle.center());
    }

    @Test
    void when_withInvalidRadius_shouldThrowException() {
      Vector2D center = new Vector2D(0, 0);

      assertThrows(IllegalArgumentException.class, () -> new Circle(-1, center));
    }
  }
}
