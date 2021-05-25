package technology.sola.engine.physics;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.PositionComponent;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CollisionUtilsTest {
  private final Entity mockEntityA = Mockito.mock(Entity.class);
  private final Entity mockEntityB = Mockito.mock(Entity.class);

  @Nested
  class calculateAABBVsAABB {
    private final ColliderComponent aabbColliderComponent = ColliderComponent.rectangle(5, 5);

    @Test
    void whenNotOverlappingX_shouldReturnNull() {
      PositionComponent positionComponentA = new PositionComponent(0, 0);
      PositionComponent positionComponentB = new PositionComponent(6, 2);

      CollisionManifold collisionManifold =
        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionComponentA, positionComponentB, aabbColliderComponent, aabbColliderComponent);

      assertNull(collisionManifold);
    }

    @Test
    void whenOverlappingXAndNotY_shouldReturnNull() {
      PositionComponent positionComponentA = new PositionComponent(0, 0);
      PositionComponent positionComponentB = new PositionComponent(3, 6);

      CollisionManifold collisionManifold =
        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionComponentA, positionComponentB, aabbColliderComponent, aabbColliderComponent);

      assertNull(collisionManifold);
    }

    @Test
    void whenOverlapping_withPositivePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
      PositionComponent positionComponentA = new PositionComponent(0, 0);
      PositionComponent positionComponentB = new PositionComponent(4, 1);

      CollisionManifold collisionManifold =
        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionComponentA, positionComponentB, aabbColliderComponent, aabbColliderComponent);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(1, 0), collisionManifold.getNormal());
      assertEquals(1, collisionManifold.getPenetration());
    }

    @Test
    void whenOverlapping_withNegativePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
      PositionComponent positionComponentA = new PositionComponent(0, 0);
      PositionComponent positionComponentB = new PositionComponent(-4, 1);

      CollisionManifold collisionManifold =
        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionComponentA, positionComponentB, aabbColliderComponent, aabbColliderComponent);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-1, 0), collisionManifold.getNormal());
      assertEquals(1, collisionManifold.getPenetration());
    }

    @Test
    void whenOverlapping_withPositivePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
      PositionComponent positionComponentA = new PositionComponent(0, 0);
      PositionComponent positionComponentB = new PositionComponent(1, 4);

      CollisionManifold collisionManifold =
        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionComponentA, positionComponentB, aabbColliderComponent, aabbColliderComponent);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(0, 1), collisionManifold.getNormal());
      assertEquals(1, collisionManifold.getPenetration());
    }

    @Test
    void whenOverlapping_withNegativePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
      PositionComponent positionComponentA = new PositionComponent(0, 0);
      PositionComponent positionComponentB = new PositionComponent(1, -4);

      CollisionManifold collisionManifold =
        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionComponentA, positionComponentB, aabbColliderComponent, aabbColliderComponent);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(0, -1), collisionManifold.getNormal());
      assertEquals(1, collisionManifold.getPenetration());
    }

    @Test
    void whenOverlapping_withDifferentSizedColliders_shouldReturnCorrectManifold() {
      PositionComponent positionComponentA = new PositionComponent(0, 0);
      PositionComponent positionComponentB = new PositionComponent(1, -4);

      CollisionManifold collisionManifold =
        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionComponentA, positionComponentB, aabbColliderComponent, ColliderComponent.rectangle(10, 10));

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(1, 0), collisionManifold.getNormal());
      assertEquals(4, collisionManifold.getPenetration());
    }
  }

  @Nested
  class calculateAABBVsCircle {
    @Test
    void whenCircleCenterOutsideRectangle_shouldHaveCorrectNormal() {
      PositionComponent positionA = new PositionComponent(0, 0);
      PositionComponent positionB = new PositionComponent(-4, -4);
      ColliderComponent colliderA = ColliderComponent.rectangle(5, 3);
      ColliderComponent colliderB = ColliderComponent.circle(3);

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionA, positionB, colliderA, colliderB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-0.70710677f, -0.70710677f), collisionManifold.getNormal());
    }

    @Test
    void whenCircleCenterInsideRectangle_shouldHaveCorrectNormal() {
      PositionComponent positionA = new PositionComponent(0, 0);
      PositionComponent positionB = new PositionComponent(-2, -1);
      ColliderComponent colliderA = ColliderComponent.rectangle(5, 3);
      ColliderComponent colliderB = ColliderComponent.circle(3);

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionA, positionB, colliderA, colliderB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-1, 0), collisionManifold.getNormal());
    }
  }

  @Nested
  class calculateCircleVsCircle {
    @Test
    void whenNotOverlapping_shouldReturnNull() {
      PositionComponent positionA = new PositionComponent();
      ColliderComponent colliderA = ColliderComponent.circle(5f);

      PositionComponent positionB = new PositionComponent(10, 0);
      ColliderComponent colliderB = ColliderComponent.circle(5f);

      CollisionManifold result = CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionA, positionB, colliderA, colliderB);
      assertNull(result);
    }

    @Test
    void whenOverlapping_shouldReturnCollisionManifold() {
      PositionComponent positionA = new PositionComponent();
      ColliderComponent colliderA = ColliderComponent.circle(5f);

      PositionComponent positionB = new PositionComponent(5, 5);
      ColliderComponent colliderB = ColliderComponent.circle(5f);

      CollisionManifold result = CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionA, positionB, colliderA, colliderB);
      assertNotNull(result);
      assertEquals(2.9289322f, result.getPenetration());
      assertEquals(new Vector2D(0.7071068f, 0.7071068f), result.getNormal());
    }
  }
}

