package technology.sola.engine.physics.utils;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CollisionUtilsTest {
  @Nested
  class calculateAABBVsAABB {
    @Test
    void whenNotOverlappingX_shouldReturnNull() {
      Rectangle rectangleA = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));
      Rectangle rectangleB = new Rectangle(new Vector2D(6, 2), new Vector2D(11, 7));

      var minimumTranslationVector = CollisionUtils.calculateAABBVsAABB(rectangleA, rectangleB);

      assertNull(minimumTranslationVector);
    }

    @Test
    void whenOverlappingXAndNotY_shouldReturnNull() {
      Rectangle rectangleA = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));
      Rectangle rectangleB = new Rectangle(new Vector2D(3, 6), new Vector2D(8, 11));

      var minimumTranslationVector = CollisionUtils.calculateAABBVsAABB(rectangleA, rectangleB);

      assertNull(minimumTranslationVector);
    }

    @Test
    void whenOverlapping_withPositivePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
      Rectangle rectangleA = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));
      Rectangle rectangleB = new Rectangle(new Vector2D(4, 1), new Vector2D(9, 6));

      var minimumTranslationVector = CollisionUtils.calculateAABBVsAABB(rectangleA, rectangleB);

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(1, 0), minimumTranslationVector.normal());
      assertEquals(1, minimumTranslationVector.penetration());
    }

    @Test
    void whenOverlapping_withNegativePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
      Rectangle rectangleA = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));
      Rectangle rectangleB = new Rectangle(new Vector2D(-4, 1), new Vector2D(1, 6));

      var minimumTranslationVector = CollisionUtils.calculateAABBVsAABB(rectangleA, rectangleB);

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(-1, 0), minimumTranslationVector.normal());
      assertEquals(1, minimumTranslationVector.penetration());
    }

    @Test
    void whenOverlapping_withPositivePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
      Rectangle rectangleA = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));
      Rectangle rectangleB = new Rectangle(new Vector2D(1, 4), new Vector2D(6, 9));

      var minimumTranslationVector = CollisionUtils.calculateAABBVsAABB(rectangleA, rectangleB);

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(0, 1), minimumTranslationVector.normal());
      assertEquals(1, minimumTranslationVector.penetration());
    }

    @Test
    void whenOverlapping_withNegativePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
      Rectangle rectangleA = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));
      Rectangle rectangleB = new Rectangle(new Vector2D(1, -4), new Vector2D(6, 1));

      var minimumTranslationVector = CollisionUtils.calculateAABBVsAABB(rectangleA, rectangleB);

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(0, -1), minimumTranslationVector.normal());
      assertEquals(1, minimumTranslationVector.penetration());
    }

    @Test
    void whenOverlapping_withDifferentSizedColliders_shouldReturnCorrectManifold() {
      Rectangle rectangleA = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));
      Rectangle rectangleB = new Rectangle(new Vector2D(1, -4), new Vector2D(11, 6));

      var minimumTranslationVector = CollisionUtils.calculateAABBVsAABB(rectangleA, rectangleB);

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(1, 0), minimumTranslationVector.normal());
      assertEquals(4, minimumTranslationVector.penetration());
    }

    @Test
    @Disabled("This test should be re-enabled once bug is fixed")
    void whenFullyInside_shouldReturnCorrectManifold() {
      Rectangle rectangleA = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 5));
      Rectangle rectangleB = new Rectangle(new Vector2D(2, 1), new Vector2D(3, 2));

      var minimumTranslationVector = CollisionUtils.calculateAABBVsAABB(rectangleA, rectangleB);

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(0, 1), minimumTranslationVector.normal());
      assertEquals(2, minimumTranslationVector.penetration());
    }
  }

  @Nested
  class calculateAABBVsCircle {
    @Test
    void whenCircleCenterOutsideRectangle_shouldHaveCorrectNormal() {
      Rectangle rectangle = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 3));
      Circle circle = new Circle(3, new Vector2D(-1,-1));

      var minimumTranslationVector = CollisionUtils.calculateAABBVsCircle(rectangle, circle);

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(-0.70710677f, -0.70710677f), minimumTranslationVector.normal());
      assertEquals(1.5857865f, minimumTranslationVector.penetration());
    }

    @Test
    void whenCircleFullyInsideRectangle_shouldHaveCorrectNormal() {
      Rectangle rectangle = new Rectangle(new Vector2D(0, 0), new Vector2D(50, 30));
      Circle circle = new Circle(3, new Vector2D(13, 14));

      var minimumTranslationVector = CollisionUtils.calculateAABBVsCircle(rectangle, circle);

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(-1, 0), minimumTranslationVector.normal());
      assertEquals(16.0, minimumTranslationVector.penetration());
    }
  }

  @Nested
  class calculateCircleVsCircle {
    @Test
    void whenNotOverlapping_shouldReturnNull() {
      var circleA = new Circle(5, new Vector2D(0, 0));
      var circleB = new Circle(5, new Vector2D(10, 0));

      var minimumTranslationVector = CollisionUtils.calculateCircleVsCircle(circleA, circleB);

      assertNull(minimumTranslationVector);
    }

    @Test
    void whenOverlapping_shouldReturnminimumTranslationVector() {
      var circleA = new Circle(5, new Vector2D(0, 0));
      var circleB = new Circle(5, new Vector2D(5, 5));
      var minimumTranslationVector = CollisionUtils.calculateCircleVsCircle(circleA, circleB);

      assertNotNull(minimumTranslationVector);
      assertEquals(2.9289322f, minimumTranslationVector.penetration());
      assertEquals(new Vector2D(0.7071068f, 0.7071068f), minimumTranslationVector.normal());
    }
  }
}

