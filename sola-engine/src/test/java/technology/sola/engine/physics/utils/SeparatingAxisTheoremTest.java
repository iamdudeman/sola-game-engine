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
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(6, 2), 5, 5)
      );

      assertNull(collisionManifold);
    }

    @Test
    void whenOverlappingXAndNotY_shouldReturnNull() {
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(3, 6), 5, 5)
      );

      assertNull(collisionManifold);
    }

    @Test
    void whenOverlapping_withPositivePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(4, 1), 5, 5)
      );

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(1, 0), collisionManifold.normal());
      assertEquals(1, collisionManifold.penetration());
    }

    @Test
    void whenOverlapping_withNegativePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(-4, 1), 5, 5)
      );

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-1, 0), collisionManifold.normal());
      assertEquals(1, collisionManifold.penetration());
    }

    @Test
    void whenOverlapping_withPositivePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(1, 4), 5, 5)
      );

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(0, 1), collisionManifold.normal());
      assertEquals(1, collisionManifold.penetration());
    }

    @Test
    void whenOverlapping_withNegativePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(1, -4), 5, 5)
      );

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(0, -1), collisionManifold.normal());
      assertEquals(1, collisionManifold.penetration());
    }

    @Test
    void whenOverlapping_withDifferentSizedColliders_shouldReturnCorrectManifold() {
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        aabbShape,
        createAABB(new Vector2D(1, -4), 10, 10)
      );

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(1, 0), collisionManifold.normal());
      assertEquals(4, collisionManifold.penetration());
    }
  }

  @Nested
  class calculateAABBVsCircle {
    @Test
    void whenCircleCenterOutsideRectangle_shouldHaveCorrectNormal() {
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        createAABB(new Vector2D(0, 0), 5, 3),
        new Vector2D(-1, -1), 3
      );

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-0.70710677f, -0.70710677f), collisionManifold.normal());
      assertEquals(1.5857862f, collisionManifold.penetration());
    }

    @Test
    void whenCircleFullyInsideRectangle_shouldHaveCorrectNormal() {
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        createAABB(new Vector2D(0, 0), 50, 30),
        new Vector2D(13, 14), 3
      );

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-1, 0), collisionManifold.normal());
      assertEquals(16, collisionManifold.penetration());
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
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        triangle,
        new Vector2D(0, 3), 1
      );

      assertNull(collisionManifold);
    }

    @Test
    void whenCircleOverlappingTriangle_shouldBeColliding() {
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        triangle,
        new Vector2D(1, 3), 1
      );

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-0.857493f, 0.5144958f), collisionManifold.normal());
      assertEquals(0.31400573f, collisionManifold.penetration());
    }

    @Test
    void whenCircleFullyInsideTriangle_shouldBeColliding() {
      var collisionManifold = SeparatingAxisTheorem.checkCollision(
        triangle,
        new Vector2D(2, 1), 0.5f
      );

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(0, -1f), collisionManifold.normal());
      assertEquals(1.5f, collisionManifold.penetration());
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
