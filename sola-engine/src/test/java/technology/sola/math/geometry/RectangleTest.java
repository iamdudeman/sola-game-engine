package technology.sola.math.geometry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.*;

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

  @Nested
  class getCenter {
    @Test
    void when_shouldReturnCenter() {
      assertEquals(
        new Vector2D(2.5f, 2.5f),
        new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5)).getCenter()
      );

      assertEquals(
        new Vector2D(-2.5f, -2.5f),
        new Rectangle(new Vector2D(-5, -5), new Vector2D(0, 0)).getCenter()
      );
    }
  }

  @Nested
  class contains {
    @Test
    void whenPointInside_shouldReturnTrue() {
      Rectangle rectangle = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));

      assertTrue(rectangle.contains(new Vector2D(0, 0)));
      assertTrue(rectangle.contains(new Vector2D(3, 3)));
      assertTrue(rectangle.contains(new Vector2D(5, 5)));
    }

    @Test
    void whenPointOutside_shouldReturnFalse() {
      Rectangle rectangle = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));

      assertFalse(rectangle.contains(new Vector2D(-0.1f, 0)));
      assertFalse(rectangle.contains(new Vector2D(0, -0.1f)));
      assertFalse(rectangle.contains(new Vector2D(5.1f, 5)));
      assertFalse(rectangle.contains(new Vector2D(5, 5.1f)));
    }
  }

  @Nested
  class intersects {
    @Test
    void whenOverlapping_shouldReturnTrue() {
      Rectangle rectangle = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));

      assertTrue(rectangle.intersects(new Rectangle(new Vector2D(5, 5), new Vector2D(10, 10))));
      assertTrue(rectangle.intersects(new Rectangle(new Vector2D(-5, -5), new Vector2D(0, 0))));
      assertTrue(rectangle.intersects(new Rectangle(new Vector2D(2, 2), new Vector2D(4, 4))));
    }

    @Test
    void whenNotOverlapping_shouldReturnFalse() {
      Rectangle rectangle = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));

      assertFalse(rectangle.intersects(new Rectangle(new Vector2D(5.1f, 5), new Vector2D(10, 10))));
      assertFalse(rectangle.intersects(new Rectangle(new Vector2D(5, 5.1f), new Vector2D(10, 10))));
      assertFalse(rectangle.intersects(new Rectangle(new Vector2D(-5, -5), new Vector2D(-0.1f, 0))));
      assertFalse(rectangle.intersects(new Rectangle(new Vector2D(-5, -5), new Vector2D(0, -0.1f))));
    }
  }
}
