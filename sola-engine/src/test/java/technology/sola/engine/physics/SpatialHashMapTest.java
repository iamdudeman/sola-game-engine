package technology.sola.engine.physics;

import org.junit.jupiter.api.Test;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.component.ColliderComponent;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpatialHashMapTest {
  @Test
  void whenNoEntitiesWithCollider_withCellSizeSpecified_shouldNotThrowException() {
    assertDoesNotThrow(() -> new SpatialHashMap(Collections.emptyList(), 500));
  }

//  @Test
//  void whenEntitiesHaveNoColliderComponent_shouldNotThrowException() {
//    World world = new World(2);
//    world.createEntity().addComponent(new TransformComponent());
//    world.createEntity().addComponent(new TransformComponent());
//
//    assertDoesNotThrow(() -> new SpatialHashMap(world.getEnabledEntities()));
//  }
//
//  @Test
//  void whenEntitiesHaveNoPositionComponent_shouldNotThrowException() {
//    World world = new World(2);
//    world.createEntity().addComponent(ColliderComponent.aabb(10, 10));
//    world.createEntity().addComponent(ColliderComponent.aabb(10, 10));
//
//    assertDoesNotThrow(() -> new SpatialHashMap(world.getEnabledEntities()));
//  }
//
//  @Test
//  void whenCreatedWithCellSize_shouldUsePassedValue() {
//    World world = createTestWorld();
//    SpatialHashMap spatialHashMap = new SpatialHashMap(world.getEnabledEntities(), 45);
//
//    assertEquals(45, spatialHashMap.getCellSize());
//  }
//
//  @Test
//  void whenCreatedWithCellSize_shouldNowAllowSmallerThanLargestEntity() {
//    World world = createTestWorld();
//
//    assertThrows(IllegalArgumentException.class, () -> new SpatialHashMap(world.getEnabledEntities(), 19));
//  }
//
//  @Test
//  void whenCreatedWithoutCellSize_shouldUseLargestEntityWidthOrHeight() {
//    World world = createTestWorld();
//    SpatialHashMap spatialHashMap = new SpatialHashMap(world.getEnabledEntities());
//
//    assertEquals(40, spatialHashMap.getCellSize());
//  }
//
//  @Test
//  void whenEntitiesRegistered_shouldProperlyPlaceInBuckets() {
//    World world = createTestWorld();
//    List<Entity> entities = world.getEnabledEntities();
//    SpatialHashMap spatialHashMap = new SpatialHashMap(entities);
//
//    SpatialHashMap.BucketId[] bucketIds = spatialHashMap.getBucketIdsForEntity(entities.get(0));
//    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[0]);
//    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[1]);
//    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[2]);
//    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[3]);
//
//    bucketIds = spatialHashMap.getBucketIdsForEntity(entities.get(1));
//    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[0]);
//    assertEquals(new SpatialHashMap.BucketId(1, 0), bucketIds[1]);
//    assertEquals(new SpatialHashMap.BucketId(0, 1), bucketIds[2]);
//    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[3]);
//
//    bucketIds = spatialHashMap.getBucketIdsForEntity(entities.get(2));
//    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[0]);
//    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[1]);
//    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[2]);
//    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[3]);
//
//    bucketIds = spatialHashMap.getBucketIdsForEntity(entities.get(3));
//    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[0]);
//    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[1]);
//    assertEquals(new SpatialHashMap.BucketId(0, 1), bucketIds[2]);
//    assertEquals(new SpatialHashMap.BucketId(0, 1), bucketIds[3]);
//
//    bucketIds = spatialHashMap.getBucketIdsForEntity(entities.get(4));
//    assertEquals(new SpatialHashMap.BucketId(1, 0), bucketIds[0]);
//    assertEquals(new SpatialHashMap.BucketId(1, 0), bucketIds[1]);
//    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[2]);
//    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[3]);
//  }
//
//  @Test
//  void whenEntitiesRegistered_shouldHaveProperNearbyEntities() {
//    World world = createTestWorld();
//    List<Entity> entities = world.getEnabledEntities();
//    SpatialHashMap spatialHashMap = new SpatialHashMap(entities);
//
//    List<Entity> nearbyEntities = spatialHashMap.getNearbyEntities(entities.get(0));
//    assertTrue(nearbyEntities.contains(entities.get(1)));
//    assertTrue(nearbyEntities.contains(entities.get(3)));
//
//    nearbyEntities = spatialHashMap.getNearbyEntities(entities.get(1));
//    assertTrue(nearbyEntities.contains(entities.get(0)));
//    assertTrue(nearbyEntities.contains(entities.get(3)));
//    assertTrue(nearbyEntities.contains(entities.get(4)));
//
//    nearbyEntities = spatialHashMap.getNearbyEntities(entities.get(2));
//    assertTrue(nearbyEntities.contains(entities.get(1)));
//    assertTrue(nearbyEntities.contains(entities.get(4)));
//
//
//    nearbyEntities = spatialHashMap.getNearbyEntities(entities.get(3));
//    assertTrue(nearbyEntities.contains(entities.get(0)));
//    assertTrue(nearbyEntities.contains(entities.get(1)));
//
//    nearbyEntities = spatialHashMap.getNearbyEntities(entities.get(4));
//    assertTrue(nearbyEntities.contains(entities.get(1)));
//    assertTrue(nearbyEntities.contains(entities.get(2)));
//  }

  private World createTestWorld() {
    World world = new World(5);

    world.createEntity()
      .addComponent(new TransformComponent(0, 0))
      .addComponent(ColliderComponent.aabb(10, 10));

    world.createEntity()
      .addComponent(new TransformComponent(25, 25))
      .addComponent(ColliderComponent.aabb(20, 20));

    world.createEntity()
      .addComponent(new TransformComponent(55, 55))
      .addComponent(ColliderComponent.aabb(15, 15));

    world.createEntity()
      .addComponent(new TransformComponent(25, 35))
      .addComponent(ColliderComponent.aabb(10, 10));

    world.createEntity()
      .addComponent(new TransformComponent(45, 35))
      .addComponent(ColliderComponent.aabb(10, 10));

    return world;
  }
}

