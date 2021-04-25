package technology.sola.math;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolKanaMathTest {
  @Nested
  class clamp {
    @Nested
    class floats {
      @Test
      void whenSmaller_shouldUseMin() {
        float result = SolKanaMath.clamp(0, 10, -5);

        assertEquals(0, result);
      }

      @Test
      void whenLarger_shouldUseMax() {
        float result = SolKanaMath.clamp(0, 10, 12);

        assertEquals(10, result);
      }

      @Test
      void whenBetween_shouldUseValue() {
        float result = SolKanaMath.clamp(0, 10, 6);

        assertEquals(6, result);
      }
    }

    @Nested
    class vectors {
      private final Vector2D min = new Vector2D(0, 0);
      private final Vector2D max = new Vector2D(10, 10);

      @Test
      void whenSmaller_shouldUseMin() {
        Vector2D result = SolKanaMath.clamp(min, max, new Vector2D(-5, -4));

        assertEquals(min, result);
      }

      @Test
      void whenLarger_shouldUseMax() {
        Vector2D result = SolKanaMath.clamp(min, max, new Vector2D(11, 12));

        assertEquals(max, result);
      }

      @Test
      void whenBetween_shouldUseValue() {
        Vector2D result = SolKanaMath.clamp(min, max, new Vector2D(5, 4));

        assertEquals(new Vector2D(5, 4), result);
      }
    }
  }
}
