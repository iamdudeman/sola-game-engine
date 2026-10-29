package technology.sola.math.geometry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.*;

class EllipseTest {
  @Nested
  class constructor {
    @Test
    void when_shouldSetValues() {
      Ellipse ellipse = new Ellipse(20, 40);

      assertEquals(20, ellipse.width());
      assertEquals(40, ellipse.height());
    }

    @Test
    void when_withInvalidDimensions_shouldThrowException() {
      assertThrows(IllegalArgumentException.class, () -> new Ellipse(-20, 40));
      assertThrows(IllegalArgumentException.class, () -> new Ellipse(20, -40));
    }
  }

  @Nested
  class contains {
    @Test
    void when_pointInsideEllipse_shouldReturnTrue() {
      Ellipse ellipse = new Ellipse(20, 40);

      assertTrue(ellipse.contains(new Vector2D(10, 20)));
      assertTrue(ellipse.contains(new Vector2D(10, 5)));
      assertTrue(ellipse.contains(new Vector2D(15, 20)));
    }

    @Test
    void when_pointOutsideEllipse_shouldReturnFalse() {
      Ellipse ellipse = new Ellipse(20, 40);

      assertFalse(ellipse.contains(new Vector2D(20, 40)));
      assertFalse(ellipse.contains(new Vector2D(0, 0)));
      assertFalse(ellipse.contains(new Vector2D(25, 20)));
      assertFalse(ellipse.contains(new Vector2D(10, 45)));
    }

    @Test
    void when_degenerateEllipse_shouldReturnFalse() {
      assertFalse(new Ellipse(0, 40).contains(new Vector2D(0, 20)));
      assertFalse(new Ellipse(20, 0).contains(new Vector2D(10, 0)));
      assertFalse(new Ellipse(0, 0).contains(new Vector2D(0, 0)));
    }
  }

  @Nested
  class getCentroid {
    @Test
    void when_shouldReturnCentroid() {
      Ellipse ellipse = new Ellipse(20, 40);

      assertEquals(new Vector2D(10, 20), ellipse.getCentroid());
    }
  }

  @Nested
  class getArea {
    @Test
    void when_shouldReturnArea() {
      Ellipse ellipse = new Ellipse(20, 40);

      assertEquals((float) (Math.PI * 10 * 20), ellipse.getArea());
    }

    @Test
    void when_degenerateEllipse_shouldReturnZero() {
      assertEquals(0, new Ellipse(0, 40).getArea());
      assertEquals(0, new Ellipse(20, 0).getArea());
      assertEquals(0, new Ellipse(0, 0).getArea());
    }
  }
}
