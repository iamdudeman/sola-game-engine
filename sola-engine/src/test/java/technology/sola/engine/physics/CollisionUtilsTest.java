package technology.sola.engine.physics;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.ecs.Entity;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CollisionUtilsTest {
  private final Entity mockEntityA = Mockito.mock(Entity.class);
  private final Entity mockEntityB = Mockito.mock(Entity.class);

//  @Nested
//  class calculateAABBVsAABB {
//    private final ColliderComponent aabbColliderComponent = ColliderComponent.aabb(5, 5);
//
//    @Test
//    void whenNotOverlappingX_shouldReturnNull() {
//      TransformComponent transformA = new TransformComponent(0, 0);
//      TransformComponent transformB = new TransformComponent(6, 2);
//
//      CollisionManifold collisionManifold =
//        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, transformA, transformB, aabbColliderComponent, aabbColliderComponent);
//
//      assertNull(collisionManifold);
//    }
//
//    @Test
//    void whenOverlappingXAndNotY_shouldReturnNull() {
//      TransformComponent transformA = new TransformComponent(0, 0);
//      TransformComponent transformB = new TransformComponent(3, 6);
//
//      CollisionManifold collisionManifold =
//        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, transformA, transformB, aabbColliderComponent, aabbColliderComponent);
//
//      assertNull(collisionManifold);
//    }
//
//    @Test
//    void whenOverlapping_withPositivePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
//      TransformComponent transformA = new TransformComponent(0, 0);
//      TransformComponent transformB = new TransformComponent(4, 1);
//
//      CollisionManifold collisionManifold =
//        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, transformA, transformB, aabbColliderComponent, aabbColliderComponent);
//
//      assertNotNull(collisionManifold);
//      assertEquals(new Vector2D(1, 0), collisionManifold.normal());
//      assertEquals(1, collisionManifold.penetration());
//    }
//
//    @Test
//    void whenOverlapping_withNegativePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
//      TransformComponent transformA = new TransformComponent(0, 0);
//      TransformComponent transformB = new TransformComponent(-4, 1);
//
//      CollisionManifold collisionManifold =
//        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, transformA, transformB, aabbColliderComponent, aabbColliderComponent);
//
//      assertNotNull(collisionManifold);
//      assertEquals(new Vector2D(-1, 0), collisionManifold.normal());
//      assertEquals(1, collisionManifold.penetration());
//    }
//
//    @Test
//    void whenOverlapping_withPositivePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
//      TransformComponent transformA = new TransformComponent(0, 0);
//      TransformComponent transformB = new TransformComponent(1, 4);
//
//      CollisionManifold collisionManifold =
//        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, transformA, transformB, aabbColliderComponent, aabbColliderComponent);
//
//      assertNotNull(collisionManifold);
//      assertEquals(new Vector2D(0, 1), collisionManifold.normal());
//      assertEquals(1, collisionManifold.penetration());
//    }
//
//    @Test
//    void whenOverlapping_withNegativePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
//      TransformComponent transformA = new TransformComponent(0, 0);
//      TransformComponent transformB = new TransformComponent(1, -4);
//
//      CollisionManifold collisionManifold =
//        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, transformA, transformB, aabbColliderComponent, aabbColliderComponent);
//
//      assertNotNull(collisionManifold);
//      assertEquals(new Vector2D(0, -1), collisionManifold.normal());
//      assertEquals(1, collisionManifold.penetration());
//    }
//
//    @Test
//    void whenOverlapping_withDifferentSizedColliders_shouldReturnCorrectManifold() {
//      TransformComponent transformA = new TransformComponent(0, 0);
//      TransformComponent transformB = new TransformComponent(1, -4);
//
//      CollisionManifold collisionManifold =
//        CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, transformA, transformB, aabbColliderComponent, ColliderComponent.aabb(10, 10));
//
//      assertNotNull(collisionManifold);
//      assertEquals(new Vector2D(1, 0), collisionManifold.normal());
//      assertEquals(4, collisionManifold.penetration());
//    }
//  }
//
//  @Nested
//  class calculateAABBVsCircle {
//    @Test
//    void whenCircleCenterOutsideRectangle_shouldHaveCorrectNormal() {
//      TransformComponent positionA = new TransformComponent(0, 0);
//      TransformComponent positionB = new TransformComponent(-4, -4);
//      ColliderComponent colliderA = ColliderComponent.aabb(5, 3);
//      ColliderComponent colliderB = ColliderComponent.circle(3);
//
//      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionA, positionB, colliderA, colliderB);
//
//      assertNotNull(collisionManifold);
//      assertEquals(new Vector2D(-0.70710677f, -0.70710677f), collisionManifold.normal());
//    }
//
//    @Test
//    void whenCircleCenterInsideRectangle_shouldHaveCorrectNormal() {
//      TransformComponent positionA = new TransformComponent(0, 0);
//      TransformComponent positionB = new TransformComponent(10, 10);
//      ColliderComponent colliderA = ColliderComponent.aabb(50, 30);
//      ColliderComponent colliderB = ColliderComponent.circle(3);
//
//      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionA, positionB, colliderA, colliderB);
//
//      assertNotNull(collisionManifold);
//      assertEquals(new Vector2D(-1, 0), collisionManifold.normal());
//    }
//  }
//
//  @Nested
//  class calculateCircleVsCircle {
//    @Test
//    void whenNotOverlapping_shouldReturnNull() {
//      TransformComponent positionA = new TransformComponent();
//      ColliderComponent colliderA = ColliderComponent.circle(5f);
//
//      TransformComponent positionB = new TransformComponent(10, 0);
//      ColliderComponent colliderB = ColliderComponent.circle(5f);
//
//      CollisionManifold result = CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionA, positionB, colliderA, colliderB);
//      assertNull(result);
//    }
//
//    @Test
//    void whenOverlapping_shouldReturnCollisionManifold() {
//      TransformComponent positionA = new TransformComponent();
//      ColliderComponent colliderA = ColliderComponent.circle(5f);
//
//      TransformComponent positionB = new TransformComponent(5, 5);
//      ColliderComponent colliderB = ColliderComponent.circle(5f);
//
//      CollisionManifold result = CollisionUtils.calculateCollisionManifold(mockEntityA, mockEntityB, positionA, positionB, colliderA, colliderB);
//      assertNotNull(result);
//      assertEquals(2.9289322f, result.penetration());
//      assertEquals(new Vector2D(0.7071068f, 0.7071068f), result.normal());
//    }
//  }
}

