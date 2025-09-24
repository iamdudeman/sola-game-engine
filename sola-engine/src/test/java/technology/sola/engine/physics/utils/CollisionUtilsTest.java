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
import technology.sola.engine.physics.component.collider.ColliderShapeTriangle;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CollisionUtilsTest {
  @Nested
  class calculateCollisionManifold {
    private final Entity mockEntityA = Mockito.mock(Entity.class);
    private final Entity mockEntityB = Mockito.mock(Entity.class);

    @Test
    void aabbVsAabb() {
      ColliderComponent aabbColliderComponent = new ColliderComponent(new ColliderShapeAABB(5, 5));
      var viewEntryA = new View2Entry<>(mockEntityA, aabbColliderComponent, new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, aabbColliderComponent, new TransformComponent(4, 1));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(1, 0), collisionManifold.normal());
      assertEquals(1, collisionManifold.penetration());
      assertEquals(mockEntityA, collisionManifold.entityA());
      assertEquals(mockEntityB, collisionManifold.entityB());

      collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryB, viewEntryA);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-1, 0), collisionManifold.normal());
      assertEquals(1, collisionManifold.penetration());
      assertEquals(mockEntityB, collisionManifold.entityA());
      assertEquals(mockEntityA, collisionManifold.entityB());
    }

    @Test
    void aabbVsCircle() {
      var viewEntryA = new View2Entry<>(mockEntityA, new ColliderComponent(new ColliderShapeAABB(50, 30)), new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, new ColliderComponent(new ColliderShapeCircle(3)), new TransformComponent(10, 11));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-1, 0), collisionManifold.normal());
      assertEquals(16.0, collisionManifold.penetration());
      assertEquals(mockEntityA, collisionManifold.entityA());
      assertEquals(mockEntityB, collisionManifold.entityB());

      CollisionUtils.calculateCollisionManifold(viewEntryB, viewEntryA);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-1, 0), collisionManifold.normal());
      assertEquals(16.0, collisionManifold.penetration());
      assertEquals(mockEntityA, collisionManifold.entityA());
      assertEquals(mockEntityB, collisionManifold.entityB());
    }

    @Test
    void circleVsCircle() {
      var viewEntryA = new View2Entry<>(mockEntityA, new ColliderComponent(new ColliderShapeCircle(5f)), new TransformComponent(0, 0));
      var viewEntryB = new View2Entry<>(mockEntityB, new ColliderComponent(new ColliderShapeCircle(5f)), new TransformComponent(5, 5));

      CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(0.7071068f, 0.7071068f), collisionManifold.normal());
      assertEquals(2.9289322f, collisionManifold.penetration());
      assertEquals(mockEntityA, collisionManifold.entityA());
      assertEquals(mockEntityB, collisionManifold.entityB());

      collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryB, viewEntryA);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-0.7071068f, -0.7071068f), collisionManifold.normal());
      assertEquals(2.9289322f, collisionManifold.penetration());
      assertEquals(mockEntityB, collisionManifold.entityA());
      assertEquals(mockEntityA, collisionManifold.entityB());
    }

    @Test
    void circleVsTriangle() {
      var triangle = new Triangle(new Vector2D(0, 0), new Vector2D(3, 5), new Vector2D(6, 0));
      var viewEntryA = new View2Entry<>(mockEntityA, new ColliderComponent(new ColliderShapeCircle(1)), new TransformComponent(0, 2));
      var viewEntryB = new View2Entry<>(mockEntityB, new ColliderComponent(new ColliderShapeTriangle(triangle)), new TransformComponent(0, 0));

      var collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryA, viewEntryB);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-0.857493f, 0.5144958f), collisionManifold.normal());
      assertEquals(0.31400573f, collisionManifold.penetration());
      assertEquals(mockEntityB, collisionManifold.entityA());
      assertEquals(mockEntityA, collisionManifold.entityB());

      collisionManifold = CollisionUtils.calculateCollisionManifold(viewEntryB, viewEntryA);

      assertNotNull(collisionManifold);
      assertEquals(new Vector2D(-0.857493f, 0.5144958f), collisionManifold.normal());
      assertEquals(0.31400573f, collisionManifold.penetration());
      assertEquals(mockEntityB, collisionManifold.entityA());
      assertEquals(mockEntityA, collisionManifold.entityB());
    }
  }

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
    void whenFullyInside_shouldReturnCorrectManifold() {
      Rectangle rectangleA = new Rectangle(new Vector2D(0, 0), new Vector2D(50, 50));
      Rectangle rectangleB = new Rectangle(new Vector2D(5, 10), new Vector2D(15, 20));

      var minimumTranslationVector = CollisionUtils.calculateAABBVsAABB(rectangleA, rectangleB);

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(-1, 0), minimumTranslationVector.normal());
      assertEquals(15, minimumTranslationVector.penetration());

      rectangleB = new Rectangle(new Vector2D(10, 5), new Vector2D(20, 15));

      minimumTranslationVector = CollisionUtils.calculateAABBVsAABB(rectangleA, rectangleB);

      assertNotNull(minimumTranslationVector);
      assertEquals(new Vector2D(0, -1), minimumTranslationVector.normal());
      assertEquals(15, minimumTranslationVector.penetration());
    }
  }

  @Nested
  class calculateAABBVsCircle {
    @Test
    void whenCircleCenterOutsideRectangle_shouldHaveCorrectNormal() {
      Rectangle rectangle = new Rectangle(new Vector2D(0, 0), new Vector2D(5, 3));
      Circle circle = new Circle(3, new Vector2D(-1, -1));

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

