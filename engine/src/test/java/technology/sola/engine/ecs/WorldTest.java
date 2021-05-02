package technology.sola.engine.ecs;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WorldTest {
  @Test
  void whenCreated_withZeroMaxEntities_shouldThrowException() {
    assertThrows(IllegalArgumentException.class, () -> new World(0));
  }

  @Test
  void whenCreatingTooManyEntities_shouldThrowException() {
    World world = new World(2);
    int maxEntityCount = world.getMaxEntityCount();

    assertThrows(ECSException.class, () -> {
      for (int i = 0; i < maxEntityCount + 1; i++) {
        world.createEntity();
      }
    });
    assertEquals(2, world.getEntitiesWithComponents().size());
  }

  @Test
  void whenCreatingEntities_shouldHaveTotalCount() {
    World world = new World(5);

    for (int i = 0; i < 4; i++) {
      world.createEntity();
    }

    world.queueEntityForDestruction(world.getEntityById(0));
    world.cleanupDestroyedEntities();

    assertEquals(3, world.getTotalEntityCount());
  }

  @Nested
  class getEntityById {
    @Test
    void whenEntityWithIdNotCreated_shouldThrowException() {
      World world = new World(2);

      assertThrows(ECSException.class, () -> world.getEntityById(0));
    }

    @Test
    void whenEntityWithIdCreated_shouldReturnEntity() {
      World world = new World(2);
      Entity entity = world.createEntity();

      Entity result = world.getEntityById(entity.getId());

      assertEquals(entity, result);
    }
  }

  @Nested
  class getEntityByName {
    @Test
    void whenNoEntityWithName_shouldReturnNull() {
      World world = new World(2);
      world.createEntity();
      world.createEntity();

      Entity result = world.getEntityByName("test");

      assertNull(result);
    }

    @Test
    void whenEntityFoundWithName_shouldReturnEntity() {
      World world = new World(2);
      world.createEntity();
      Entity expected = world.createEntity().setName("test");

      Entity result = world.getEntityByName("test");

      assertEquals(expected, result);
    }
  }

  @Test
  void whenAddingComponentsForEntity_shouldBeAbleToGetThem() {
    World world = new World(1);
    TestComponent testComponent = new TestComponent();
    world.createEntity();

    world.addComponentForEntity(0, testComponent);

    assertEquals(testComponent, world.getComponentForEntity(0, TestComponent.class));
  }

  @Test
  void whenRemovingComponentsFromEntity_shouldNotBeAbleToGetThem() {
    World world = new World(1);
    TestComponent testComponent = new TestComponent();
    world.createEntity();

    world.addComponentForEntity(0, testComponent);
    world.removeComponent(0, TestComponent.class);

    assertNull(world.getComponentForEntity(0, TestComponent.class));
  }

  @Test
  void whenDestroyingEntity_shouldNotBeAbleToGetComponents() {
    World world = new World(1);
    TestComponent testComponent = new TestComponent();
    Entity entity = world.createEntity();

    entity.getCurrentComponents().add(TestComponent.class);
    world.addComponentForEntity(0, testComponent);
    world.queueEntityForDestruction(entity);
    world.cleanupDestroyedEntities();

    assertNull(world.getComponentForEntity(0, TestComponent.class));
  }

  @Nested
  class getEntitiesWithComponents {
    @Test
    void whenEntityHasAllComponents_shouldReturnEntity() {
      World world = new World(2);
      TestComponent testComponent = new TestComponent();
      TestComponent2 testComponent2 = new TestComponent2();
      Entity entity = world.createEntity();

      world.addComponentForEntity(entity.getId(), testComponent);
      world.addComponentForEntity(entity.getId(), testComponent2);

      List<Entity> entities = world.getEntitiesWithComponents(TestComponent.class, TestComponent2.class);
      assertEquals(1, entities.size());
      assertEquals(entity, entities.get(entity.getId()));
    }

    @Test
    void whenEntityIsMissingComponent_shouldNotReturnEntity() {
      World world = new World(2);
      TestComponent testComponent = new TestComponent();
      Entity entity = world.createEntity();

      world.addComponentForEntity(entity.getId(), testComponent);

      List<Entity> entities = world.getEntitiesWithComponents(TestComponent.class, TestComponent2.class);
      assertEquals(0, entities.size());
    }
  }

  private static class TestComponent implements Component {
  }

  private static class TestComponent2 implements Component {
  }
}
