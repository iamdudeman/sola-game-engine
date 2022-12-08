package technology.sola.math.geometry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RectangleTest {
  @Nested
  class constructor {
    @Test
    void when_shouldSetMinMax() {
      Rectangle rectangle = new Rectangle(new Vector2D(0, 0), new Vector2D(10, 10));

      assertEquals(new Vector2D(0, 0), rectangle.min());
      assertEquals(new Vector2D(10, 10), rectangle.max());
    }

    @Test
    void when_withInvalidMaxX_shouldThrowError() {
      Vector2D min = new Vector2D(0, 0);
      Vector2D max = new Vector2D(-1, 10);

      assertThrows(IllegalArgumentException.class, () -> new Rectangle(min, max));
    }

    @Test
    void when_withInvalidMaxY_shouldThrowError() {
      Vector2D min = new Vector2D(0, 0);
      Vector2D max = new Vector2D(10, -1);

      assertThrows(IllegalArgumentException.class, () -> new Rectangle(min, max));
    }

    @Test
    void when_shouldHaveCorrectWidthAndHeight() {
      Rectangle rectangle = new Rectangle(new Vector2D(2, 2), new Vector2D(10, 9));

      assertEquals(8, rectangle.getWidth());
      assertEquals(7, rectangle.getHeight());
    }
  }
}
