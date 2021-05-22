package technology.sola.engine.physics;

import org.junit.jupiter.api.Test;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.ecs.World;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.PositionComponent;
import technology.sola.math.linear.Vector2D;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpacialHashMapTest {
  @Test
  void whenNoEntitiesWithCollider_withCellSizeSpecified_shouldNotThrowException() {
    assertDoesNotThrow(() -> new SpacialHashMap(Collections.emptyList(), 500));
  }

  @Test
  void whenEntitiesHaveNoColliderComponent_shouldNotThrowException() {
    World world = new World(2);
    world.createEntity().addComponent(new PositionComponent());
    world.createEntity().addComponent(new PositionComponent());

    assertDoesNotThrow(() -> new SpacialHashMap(world.getEntitiesWithComponents()));
  }

  @Test
  void whenEntitiesHaveNoPositionComponent_shouldNotThrowException() {
    World world = new World(2);
    world.createEntity().addComponent(ColliderComponent.rectangle(10, 10));
    world.createEntity().addComponent(ColliderComponent.rectangle(10, 10));

    assertDoesNotThrow(() -> new SpacialHashMap(world.getEntitiesWithComponents()));
  }

  @Test
  void whenCreatedWithCellSize_shouldUsePassedValue() {
    World world = createTestWorld();
    SpacialHashMap spacialHashMap = new SpacialHashMap(world.getEntitiesWithComponents(), 45);

    assertEquals(45, spacialHashMap.getCellSize());
  }

  @Test
  void whenCreatedWithCellSize_shouldNowAllowSmallerThanLargestEntity() {
    World world = createTestWorld();

    assertThrows(IllegalArgumentException.class, () -> new SpacialHashMap(world.getEntitiesWithComponents(), 19));
  }

  @Test
  void whenCreatedWithoutCellSize_shouldUseLargestEntityWidthOrHeight() {
    World world = createTestWorld();
    SpacialHashMap spacialHashMap = new SpacialHashMap(world.getEntitiesWithComponents());

    assertEquals(40, spacialHashMap.getCellSize());
  }

  @Test
  void whenEntitiesRegistered_shouldProperlyPlaceInBuckets() {
    World world = createTestWorld();
    List<Entity> entities = world.getEntitiesWithComponents();
    SpacialHashMap spacialHashMap = new SpacialHashMap(entities);

    List<Vector2D> points = spacialHashMap.getBucketIdsForEntity(entities.get(0));
    assertEquals(new Vector2D(0, 0), points.get(0));
    assertEquals(new Vector2D(0, 0), points.get(1));
    assertEquals(new Vector2D(0, 0), points.get(2));
    assertEquals(new Vector2D(0, 0), points.get(3));

    points = spacialHashMap.getBucketIdsForEntity(entities.get(1));
    assertEquals(new Vector2D(0, 0), points.get(0));
    assertEquals(new Vector2D(1, 0), points.get(1));
    assertEquals(new Vector2D(0, 1), points.get(2));
    assertEquals(new Vector2D(1, 1), points.get(3));

    points = spacialHashMap.getBucketIdsForEntity(entities.get(2));
    assertEquals(new Vector2D(1, 1), points.get(0));
    assertEquals(new Vector2D(1, 1), points.get(1));
    assertEquals(new Vector2D(1, 1), points.get(2));
    assertEquals(new Vector2D(1, 1), points.get(3));

    points = spacialHashMap.getBucketIdsForEntity(entities.get(3));
    assertEquals(new Vector2D(0, 0), points.get(0));
    assertEquals(new Vector2D(0, 0), points.get(1));
    assertEquals(new Vector2D(0, 1), points.get(2));
    assertEquals(new Vector2D(0, 1), points.get(3));

    points = spacialHashMap.getBucketIdsForEntity(entities.get(4));
    assertEquals(new Vector2D(1, 0), points.get(0));
    assertEquals(new Vector2D(1, 0), points.get(1));
    assertEquals(new Vector2D(1, 1), points.get(2));
    assertEquals(new Vector2D(1, 1), points.get(3));
  }

  @Test
  void whenEntitiesRegistered_shouldHaveProperNearbyEntities() {
    World world = createTestWorld();
    List<Entity> entities = world.getEntitiesWithComponents();
    SpacialHashMap spacialHashMap = new SpacialHashMap(entities);

    List<Entity> nearbyEntities = spacialHashMap.getNearbyEntities(entities.get(0));
    assertTrue(nearbyEntities.contains(entities.get(1)));
    assertTrue(nearbyEntities.contains(entities.get(3)));

    nearbyEntities = spacialHashMap.getNearbyEntities(entities.get(1));
    assertTrue(nearbyEntities.contains(entities.get(0)));
    assertTrue(nearbyEntities.contains(entities.get(3)));
    assertTrue(nearbyEntities.contains(entities.get(4)));

    nearbyEntities = spacialHashMap.getNearbyEntities(entities.get(2));
    assertTrue(nearbyEntities.contains(entities.get(1)));
    assertTrue(nearbyEntities.contains(entities.get(4)));


    nearbyEntities = spacialHashMap.getNearbyEntities(entities.get(3));
    assertTrue(nearbyEntities.contains(entities.get(0)));
    assertTrue(nearbyEntities.contains(entities.get(1)));

    nearbyEntities = spacialHashMap.getNearbyEntities(entities.get(4));
    assertTrue(nearbyEntities.contains(entities.get(1)));
    assertTrue(nearbyEntities.contains(entities.get(2)));
  }

  private World createTestWorld() {
    World world = new World(5);

    world.createEntity()
      .addComponent(new PositionComponent(0, 0))
      .addComponent(ColliderComponent.rectangle(10, 10));

    world.createEntity()
      .addComponent(new PositionComponent(25, 25))
      .addComponent(ColliderComponent.rectangle(20, 20));

    world.createEntity()
      .addComponent(new PositionComponent(55, 55))
      .addComponent(ColliderComponent.rectangle(15, 15));

    world.createEntity()
      .addComponent(new PositionComponent(25, 35))
      .addComponent(ColliderComponent.rectangle(10, 10));

    world.createEntity()
      .addComponent(new PositionComponent(45, 35))
      .addComponent(ColliderComponent.rectangle(10, 10));

    return world;
  }
}

