package technology.sola.math.linear;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class Vector2DTest {
  @Nested
  class staticMethods {
    @Nested
    class headingVectorFromAngle {
      @Test
      void when_shouldReturnCorrectVector() {
        Vector2D headingVector = Vector2D.headingVectorFromAngle(Math.PI);

        assertEquals(-1, headingVector.x(), 0.0001);
        assertEquals(0, headingVector.y(), 0.0001);
      }
    }
  }

  @Nested
  class instanceMethods {
    @Test
    void add() {
      Vector2D vector = new Vector2D(3, 4);
      Vector2D vector2 = new Vector2D(3, 1);

      Vector2D result = vector.add(vector2);
      assertEquals(6, result.x());
      assertEquals(5, result.y());

      // Ensure the result object is a new object
      assertNotEquals(vector, result);
      assertNotEquals(vector2, result);

      // Ensure reversing the add had the same result
      Vector2D reverseResult = vector2.add(vector);
      assertEquals(result, reverseResult);
    }

    @Test
    void subtract() {
      Vector2D vector = new Vector2D(3, 4);
      Vector2D vector2 = new Vector2D(3, 1);

      Vector2D result = vector.subtract(vector2);
      assertEquals(0, result.x());
      assertEquals(3, result.y());

      // Ensure the result object is a new object
      assertNotEquals(vector, result);
      assertNotEquals(vector2, result);

      // Ensure reversing the subtract has a different result
      Vector2D reverseResult = vector2.subtract(vector);
      assertNotEquals(result, reverseResult);
    }

    @Test
    void scalar() {
      Vector2D vector = new Vector2D(2, 8);

      Vector2D result = vector.scalar(0.5f);
      assertEquals(1, result.x());
      assertEquals(4, result.y());

      result = vector.scalar(2);
      assertEquals(4, result.x());
      assertEquals(16, result.y());

      // Ensure the result object is a new object
      assertNotEquals(vector, result);
    }

    @Nested
    class magnitude {
      @Test
      void whenZeroVector_shouldReturnZero() {
        Vector2D vector = new Vector2D(0, 0);

        assertEquals(0, vector.magnitude());
      }

      @Test
      void whenNotZeroVector_shouldReturnResult() {
        Vector2D vector = new Vector2D(3, 4);

        assertEquals(5, vector.magnitude());
      }
    }

    @Nested
    class magnitudeSq {
      @Test
      void whenZeroVector_shouldReturnZero() {
        Vector2D vector = new Vector2D(0, 0);

        assertEquals(0, vector.magnitudeSq());
      }

      @Test
      void whenNotZeroVector_shouldReturnResultOfMagnitudeSquared() {
        Vector2D vector = new Vector2D(3, 4);

        assertEquals(vector.magnitude() * vector.magnitude(), vector.magnitudeSq());
      }
    }

    @Test
    void distance() {
      Vector2D vector1 = new Vector2D(-3, -4);
      Vector2D vector2 = new Vector2D(3, 4);

      assertEquals(10, vector1.distance(vector2));
    }

    @Test
    void distanceSq() {
      Vector2D vector1 = new Vector2D(-3, -4);
      Vector2D vector2 = new Vector2D(3, 4);

      assertEquals(100, vector1.distanceSq(vector2));
    }

    @Nested
    class normalize {
      @Test
      void whenZeroVector_shouldReturnZero() {
        Vector2D vector = new Vector2D(0, 0);

        assertEquals(new Vector2D(0, 0), vector.normalize());
      }

      @Test
      void whenNotZeroVector_shouldReturnProperResult() {
        Vector2D vector = new Vector2D(3, 3);

        Vector2D normalized = vector.normalize();
        assertEquals(1, normalized.magnitude());
      }
    }

    @Test
    void dot() {
      Vector2D vector = new Vector2D(2, 5);
      Vector2D vector2 = new Vector2D(3, 4);

      double result = vector.dot(vector2);
      assertEquals(26, result);
    }

    @Test
    void rotate() {
      Vector2D vector2D = new Vector2D(1, 1);

      Vector2D rotation = vector2D.rotate(Math.PI / 2);
      assertEquals(-1, rotation.x());
      assertEquals(1, rotation.y());

      rotation = rotation.rotate(Math.PI / 2);
      assertEquals(-1, rotation.x());
      assertEquals(-1, rotation.y());

      rotation = rotation.rotate(Math.PI / 2);
      assertEquals(1, rotation.x());
      assertEquals(-1, rotation.y());

      rotation = rotation.rotate(Math.PI / 2);
      assertEquals(1, rotation.x());
      assertEquals(1, rotation.y());
    }

    @Nested
    class equals {
      @Test
      void whenSameCoordinates_shouldReturnTrue() {
        assertEquals(new Vector2D(5.3f, 7.4f), new Vector2D(5.3f, 7.4f));
      }

      @Test
      void whenDifferentCoordinates_shouldReturnFalse() {
        assertNotEquals(new Vector2D(5.3f, 7.4f), new Vector2D(5.3f, 3f));
        assertNotEquals(new Vector2D(5.3f, 7.4f), new Vector2D(5f, 7.4f));
      }

      @Test
      void whenSameCoordinates_withNegativeAndPositiveZero_shouldReturnTrue() {
        assertEquals(new Vector2D(0.0f, 0.0f), new Vector2D(-0.0f, -0.0f));
      }
    }

    @Nested
    class hashCode {
      @Test
      void whenSame_shouldHaveSameHash() {
        assertEquals(new Vector2D(5.3f, 7.4f).hashCode(), new Vector2D(5.3f, 7.4f).hashCode());
      }

      @Test
      void whenDifferent_shouldHaveDifferentHash() {
        assertNotEquals(new Vector2D(5.3f, 7.4f).hashCode(), new Vector2D(5.3f, 3f).hashCode());
        assertNotEquals(new Vector2D(5.3f, 7.4f).hashCode(), new Vector2D(5, 7.4f).hashCode());
      }
    }
  }
}
