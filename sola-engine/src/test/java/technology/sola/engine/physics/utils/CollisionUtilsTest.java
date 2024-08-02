package technology.sola.engine.physics.utils;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.ecs.Entity;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;
import technology.sola.engine.physics.component.collider.ColliderShapeCircle;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CollisionUtilsTest {
  private final Entity mockEntityA = Mockito.mock(Entity.class);
  private final Entity mockEntityB = Mockito.mock(Entity.class);

  @Nested
  class calculateAABBVsAABB {
    private final ColliderComponent aabbColliderComponent = new ColliderComponent(new ColliderShapeAABB(5, 5));

    @Test
    void whenNotOverlappingX_shouldReturnNull() {
      var viewEntryA = new View2Entry<>(mockEntityA, aabbColliderComponent, new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, aabbColliderComponent, new TransformComponent(6, 2));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNull(collisionManifold);
    }

    @Test
    void whenOverlappingXAndNotY_shouldReturnNull() {
      var viewEntryA = new View2Entry<>(mockEntityA, aabbColliderComponent, new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, aabbColliderComponent, new TransformComponent(3, 6));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNull(collisionManifold);
    }

    @Test
    void whenOverlapping_withPositivePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
      var viewEntryA = new View2Entry<>(mockEntityA, aabbColliderComponent, new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, aabbColliderComponent, new TransformComponent(4, 1));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(1, 0), collisionManifold.normal());
      assertEquals(1, collisionManifold.penetration());
    }

    @Test
    void whenOverlapping_withNegativePositionDifferenceAndLessXOverlap_shouldReturnCorrectManifold() {
      var viewEntryA = new View2Entry<>(mockEntityA, aabbColliderComponent, new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, aabbColliderComponent, new TransformComponent(-4, 1));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-1, 0), collisionManifold.normal());
      assertEquals(1, collisionManifold.penetration());
    }

    @Test
    void whenOverlapping_withPositivePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
      var viewEntryA = new View2Entry<>(mockEntityA, aabbColliderComponent, new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, aabbColliderComponent, new TransformComponent(1, 4));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(0, 1), collisionManifold.normal());
      assertEquals(1, collisionManifold.penetration());
    }

    @Test
    void whenOverlapping_withNegativePositionDifferenceAndLessYOverlap_shouldReturnCorrectManifold() {
      var viewEntryA = new View2Entry<>(mockEntityA, aabbColliderComponent, new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, aabbColliderComponent, new TransformComponent(1, -4));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(0, -1), collisionManifold.normal());
      assertEquals(1, collisionManifold.penetration());
    }

    @Test
    void whenOverlapping_withDifferentSizedColliders_shouldReturnCorrectManifold() {
      var viewEntryA = new View2Entry<>(mockEntityA, aabbColliderComponent, new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, new ColliderComponent(new ColliderShapeAABB(10, 10)), new TransformComponent(1, -4));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(1, 0), collisionManifold.normal());
      assertEquals(4, collisionManifold.penetration());
    }
  }

  @Nested
  class calculateAABBVsCircle {
    @Test
    void whenCircleCenterOutsideRectangle_shouldHaveCorrectNormal() {
      var viewEntryA = new View2Entry<>(mockEntityA, new ColliderComponent(new ColliderShapeAABB(5, 3)), new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, new ColliderComponent(new ColliderShapeCircle(3)), new TransformComponent(-4, -4));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-0.70710677f, -0.70710677f), collisionManifold.normal());
      assertEquals(1.5857865f, collisionManifold.penetration());
    }

    @Test
    void whenCircleCenterInsideRectangle_shouldHaveCorrectNormal() {
      var viewEntryA = new View2Entry<>(mockEntityA, new ColliderComponent(new ColliderShapeAABB(50, 30)), new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, new ColliderComponent(new ColliderShapeCircle(3)), new TransformComponent(10, 11));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-1, 0), collisionManifold.normal());
      assertEquals(-10.0, collisionManifold.penetration());
    }
  }

  @Nested
  class calculateCircleVsCircle {
    @Test
    void whenNotOverlapping_shouldReturnNull() {
      var viewEntryA = new View2Entry<>(mockEntityA, new ColliderComponent(new ColliderShapeCircle(5f)), new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, new ColliderComponent(new ColliderShapeCircle(5f)), new TransformComponent(10, 0));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNull(collisionManifold);
    }

    @Test
    void whenOverlapping_shouldReturnCollisionManifold() {
      var viewEntryA = new View2Entry<>(mockEntityA, new ColliderComponent(new ColliderShapeCircle(5f)), new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, new ColliderComponent(new ColliderShapeCircle(5f)), new TransformComponent(5, 5));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(2.9289322f, collisionManifold.penetration());
      assertEquals(new Vector2D(0.7071068f, 0.7071068f), collisionManifold.normal());
    }
  }
}

