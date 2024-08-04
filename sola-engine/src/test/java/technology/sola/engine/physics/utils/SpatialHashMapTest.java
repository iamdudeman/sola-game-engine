package technology.sola.engine.physics.utils;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.ecs.Entity;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;

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

  @Test
  void whenCreatedWithCellSize_shouldUsePassedValue() {
    SpatialHashMap spatialHashMap = new SpatialHashMap(createTestView(), 45);

    assertEquals(45, spatialHashMap.getCellSize());
  }

  @Test
  void whenCreatedWithCellSize_shouldNowAllowSmallerThanLargestEntity() {
    assertThrows(IllegalArgumentException.class, () -> new SpatialHashMap(createTestView(), 19));
  }

  @Test
  void whenCreatedWithoutCellSize_shouldUseLargestEntityWidthOrHeight() {
    SpatialHashMap spatialHashMap = new SpatialHashMap(createTestView());

    assertEquals(30, spatialHashMap.getCellSize());
  }

  @Test
  void whenEntitiesRegistered_shouldProperlyPlaceInBuckets() {
    var testView = createTestView();
    SpatialHashMap spatialHashMap = new SpatialHashMap(testView, 40);

    SpatialHashMap.BucketId[] bucketIds = spatialHashMap.getBucketIdsForViewEntry(testView.get(0));
    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[0]);
    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[1]);
    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[2]);
    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[3]);

    bucketIds = spatialHashMap.getBucketIdsForViewEntry(testView.get(1));
    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[0]);
    assertEquals(new SpatialHashMap.BucketId(1, 0), bucketIds[1]);
    assertEquals(new SpatialHashMap.BucketId(0, 1), bucketIds[2]);
    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[3]);

    bucketIds = spatialHashMap.getBucketIdsForViewEntry(testView.get(2));
    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[0]);
    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[1]);
    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[2]);
    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[3]);

    bucketIds = spatialHashMap.getBucketIdsForViewEntry(testView.get(3));
    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[0]);
    assertEquals(new SpatialHashMap.BucketId(0, 0), bucketIds[1]);
    assertEquals(new SpatialHashMap.BucketId(0, 1), bucketIds[2]);
    assertEquals(new SpatialHashMap.BucketId(0, 1), bucketIds[3]);

    bucketIds = spatialHashMap.getBucketIdsForViewEntry(testView.get(4));
    assertEquals(new SpatialHashMap.BucketId(1, 0), bucketIds[0]);
    assertEquals(new SpatialHashMap.BucketId(1, 0), bucketIds[1]);
    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[2]);
    assertEquals(new SpatialHashMap.BucketId(1, 1), bucketIds[3]);
  }

  @Test
  void whenEntitiesRegistered_shouldHaveProperNearbyEntities() {
    var testView = createTestView();
    SpatialHashMap spatialHashMap = new SpatialHashMap(testView, 40);

    var nearbyEntities = spatialHashMap.getNearbyViewEntries(testView.get(0));
    assertTrue(nearbyEntities.contains(testView.get(1)));
    assertTrue(nearbyEntities.contains(testView.get(3)));

    nearbyEntities = spatialHashMap.getNearbyViewEntries(testView.get(1));
    assertTrue(nearbyEntities.contains(testView.get(0)));
    assertTrue(nearbyEntities.contains(testView.get(3)));
    assertTrue(nearbyEntities.contains(testView.get(4)));

    nearbyEntities = spatialHashMap.getNearbyViewEntries(testView.get(2));
    assertTrue(nearbyEntities.contains(testView.get(1)));
    assertTrue(nearbyEntities.contains(testView.get(4)));

    nearbyEntities = spatialHashMap.getNearbyViewEntries(testView.get(3));
    assertTrue(nearbyEntities.contains(testView.get(0)));
    assertTrue(nearbyEntities.contains(testView.get(1)));

    nearbyEntities = spatialHashMap.getNearbyViewEntries(testView.get(4));
    assertTrue(nearbyEntities.contains(testView.get(1)));
    assertTrue(nearbyEntities.contains(testView.get(2)));
  }

  private List<View2Entry<ColliderComponent, TransformComponent>> createTestView() {
    return List.of(
      new View2Entry<>(Mockito.mock(Entity.class), new ColliderComponent(new ColliderShapeAABB(10, 10)), new TransformComponent(0, 0)),
      new View2Entry<>(Mockito.mock(Entity.class), new ColliderComponent(new ColliderShapeAABB(20, 20)), new TransformComponent(25, 25)),
      new View2Entry<>(Mockito.mock(Entity.class), new ColliderComponent(new ColliderShapeAABB(15, 15)), new TransformComponent(55, 55)),
      new View2Entry<>(Mockito.mock(Entity.class), new ColliderComponent(new ColliderShapeAABB(10, 10)), new TransformComponent(25, 35)),
      new View2Entry<>(Mockito.mock(Entity.class), new ColliderComponent(new ColliderShapeAABB(10, 10)), new TransformComponent(45, 35))
    );
  }
}

