package technology.sola.engine.physics;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.engine.ecs.Entity;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CollisionManifoldTest {
  @Test
  void whenSameEntities_withDifferentOrder_shouldBeEqual() {
    Entity mockEntityA = Mockito.mock(Entity.class);
    Entity mockEntityB = Mockito.mock(Entity.class);
    Mockito.when(mockEntityA.getIndexInWorld()).thenReturn(1);
    Mockito.when(mockEntityB.getIndexInWorld()).thenReturn(2);

    CollisionManifold first = new CollisionManifold(mockEntityA, mockEntityB, new Vector2D(0, 0), 0);
    CollisionManifold second = new CollisionManifold(mockEntityB, mockEntityA, new Vector2D(5, 5), 5);

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void whenDifferentEntities_shouldNotBeEqual() {
    Entity mockEntityA = Mockito.mock(Entity.class);
    Entity mockEntityB = Mockito.mock(Entity.class);
    Entity mockEntityC = Mockito.mock(Entity.class);
    Mockito.when(mockEntityA.getIndexInWorld()).thenReturn(1);
    Mockito.when(mockEntityB.getIndexInWorld()).thenReturn(2);
    Mockito.when(mockEntityC.getIndexInWorld()).thenReturn(3);

    CollisionManifold first = new CollisionManifold(mockEntityA, mockEntityB, new Vector2D(0, 0), 0);
    CollisionManifold second = new CollisionManifold(mockEntityB, mockEntityC, new Vector2D(0, 0), 0);

    assertNotEquals(first, second);
    assertNotEquals(first.hashCode(), second.hashCode());
  }
}
