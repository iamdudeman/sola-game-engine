package technology.sola.engine.physics.utils;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SeparatingAxisTheoremTest {
  @Nested
  class calculateAABBVsAABB {
    private final Vector2D[] aabbShape = createAABB(new Vector2D(0, 0), 5, 5);

    @Test
    void whenNotOverlappingX_shouldReturnNull() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(6, 2), 5, 5)
      );

      assertNull(minimumTranslationVector);
    }

    @Test
    void whenOverlappingXAndNotY_shouldReturnNull() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(3, 6), 5, 5)
      );

      assertNull(minimumTranslationVector);
    }

    @Test
    void whenOverlapping_withPositivePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(4, 1), 5, 5)
      );

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(1, 0), minimumTranslationVector.normal());
      assertEquals(1, minimumTranslationVector.penetration());
    }

    @Test
    void whenOverlapping_withNegativePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(-4, 1), 5, 5)
      );

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(-1, 0), minimumTranslationVector.normal());
      assertEquals(1, minimumTranslationVector.penetration());
    }

    @Test
    void whenOverlapping_withPositivePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(1, 4), 5, 5)
      );

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(0, 1), minimumTranslationVector.normal());
      assertEquals(1, minimumTranslationVector.penetration());
    }

    @Test
    void whenOverlapping_withNegativePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(1, -4), 5, 5)
      );

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(0, -1), minimumTranslationVector.normal());
      assertEquals(1, minimumTranslationVector.penetration());
    }

    @Test
    void whenOverlapping_withDifferentSizedColliders_shouldReturnCorrectManifold() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(1, -4), 10, 10)
      );

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(1, 0), minimumTranslationVector.normal());
      assertEquals(4, minimumTranslationVector.penetration());
    }

    @Test
    void whenFullyInside_shouldReturnCorrectManifold() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(1, 3), 2, 1)
      );

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(0, 1), minimumTranslationVector.normal());
      assertEquals(2, minimumTranslationVector.penetration());
    }
  }

  @Nested
  class calculateAABBVsCircle {
    @Test
    void whenCircleCenterOutsideRectangle_shouldHaveCorrectNormal() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        createAABB(new Vector2D(0, 0), 5, 3),
        new Vector2D(-1, -1),
        3
      );

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(-0.70710677f, -0.70710677f), minimumTranslationVector.normal());
      assertEquals(1.5857862f, minimumTranslationVector.penetration());
    }

    @Test
    void whenCircleFullyInsideRectangle_shouldHaveCorrectNormal() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        createAABB(new Vector2D(0, 0), 50, 30),
        new Vector2D(13, 14),
        3
      );

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(-1, 0), minimumTranslationVector.normal());
      assertEquals(16, minimumTranslationVector.penetration());
    }
  }

  @Nested
  class calculateCircleVsTriangle {
    Vector2D[] triangle = new Vector2D[] {
      new Vector2D(0, 0),
      new Vector2D(3, 5),
      new Vector2D(6, 0),
    };

    @Test
    void whenCircleOutsideTriangle_shouldNotBeColliding() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        triangle,
        new Vector2D(0, 3),
        1
      );

      assertNull(minimumTranslationVector);
    }

    @Test
    void whenCircleOverlappingTriangle_shouldBeColliding() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        triangle,
        new Vector2D(1, 3),
        1
      );

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(-0.857493f, 0.5144958f), minimumTranslationVector.normal());
      assertEquals(0.31400573f, minimumTranslationVector.penetration());
    }

    @Test
    void whenCircleFullyInsideTriangle_shouldBeColliding() {
      var minimumTranslationVector = SeparatingAxisTheorem.checkCollision(
        triangle,
        new Vector2D(2, 1),
        0.5f
      );

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(0, -1f), minimumTranslationVector.normal());
      assertEquals(1.5f, minimumTranslationVector.penetration());
    }
  }

  private Vector2D[] createAABB(Vector2D topLeft, float width, float height) {
    return new Vector2D[] {
      topLeft,
      topLeft.add(new Vector2D(width, 0)),
      topLeft.add(new Vector2D(width, height)),
      topLeft.add(new Vector2D(0, height)),
    };
  }
}
