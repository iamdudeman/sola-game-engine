package technology.sola.engine.physics;

import technology.sola.ecs.Entity;
import technology.sola.ecs.view.View2;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.component.ColliderComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpatialHashMap {
  private static final BucketId[] EMPTY_BUCKET_IDS = new BucketId[0];
  private final int cellSize;
  private final float inverseCellSize;
  private final Map<BucketId, List<View2Entry<ColliderComponent, TransformComponent>>> entityBuckets = new HashMap<>();

  /**
   * Creates a SpatialHashMap with cellSize set to twice the max width/height of the largest {@link Entity}.
   *
   * @param entities the list of entities being tracked by the SpatialHashMap
   */
  public SpatialHashMap(List<View2Entry<ColliderComponent, TransformComponent>> entries) {
    this.cellSize = calculateAppropriateCellSizeForEntities(entries);
    this.inverseCellSize = 1f / this.cellSize;

    entries.forEach(this::registerEntity);
  }

  /**
   * Creates a SpatialHashMap with desired cell size. Ensure that the cellSize is greater than the max width or height of the largest {@link Entity}.
   *
   * @param entities the list of entities being tracked by the SpatialHashMap
   * @param cellSize the cell size of the SpatialHashMap
   */
  public SpatialHashMap(List<View2Entry<ColliderComponent, TransformComponent>> entries, int cellSize) {
    int minimumCellSize = calculateMinSizeForEntities(entries);

    if (cellSize < minimumCellSize)
      throw new IllegalArgumentException("Cell size must be greater than largest object [" + minimumCellSize + "]");

    this.cellSize = cellSize;
    this.inverseCellSize = 1f / this.cellSize;
    entries.forEach(this::registerEntity);
  }

  public int getCellSize() {
    return cellSize;
  }

  public List<View2Entry<ColliderComponent, TransformComponent>> getNearbyEntities(View2Entry<ColliderComponent, TransformComponent> entity) {
    List<View2Entry<ColliderComponent, TransformComponent>> nearbyEntities = new ArrayList<>();

    for (BucketId bucketId : getBucketIdsForEntity(entity)) {
      List<View2Entry<ColliderComponent, TransformComponent>> bucket = getOrCreateBucket(bucketId);

      for (var entityInBucket : bucket) {
        if (entity != entityInBucket && !nearbyEntities.contains(entityInBucket)) {
          nearbyEntities.add(entityInBucket);
        }
      }
    }

    return nearbyEntities;
  }

  public Set<BucketId> getEntityBucketIds() {
    return entityBuckets.keySet();
  }

  BucketId[] getBucketIdsForEntity(View2Entry<ColliderComponent, TransformComponent> entry) {
    TransformComponent transformComponent = entry.c2();
    ColliderComponent colliderComponent = entry.c1();

    if (transformComponent == null || colliderComponent == null) return EMPTY_BUCKET_IDS;

    float x = transformComponent.getX() + colliderComponent.getOffsetX();
    float y = transformComponent.getY() + colliderComponent.getOffsetY();
    float width = colliderComponent.getBoundingWidth(transformComponent.getScaleX());
    float height = colliderComponent.getBoundingHeight(transformComponent.getScaleY());

    return new BucketId[]{
      getIdForPoint(x, y),
      getIdForPoint(x + width, y),
      getIdForPoint(x, y + height),
      getIdForPoint(x + width, y + height),
    };
  }

  private void registerEntity(View2Entry<ColliderComponent, TransformComponent> entry) {
    for (BucketId bucketId : getBucketIdsForEntity(entry)) {
      List<View2Entry<ColliderComponent, TransformComponent>> entityBucket = getOrCreateBucket(bucketId);

      if (!entityBucket.contains(entry)) {
        entityBucket.add(entry);
      }
    }
  }

  private BucketId getIdForPoint(float x, float y) {
    return new BucketId((int) Math.floor(x * inverseCellSize), (int) Math.floor(y * inverseCellSize));
  }

  private List<View2Entry<ColliderComponent, TransformComponent>> getOrCreateBucket(BucketId bucketId) {
    return entityBuckets.computeIfAbsent(bucketId, key -> new ArrayList<>());
  }

  private int calculateMinSizeForEntities(List<View2Entry<ColliderComponent, TransformComponent>> entries) {
    int minSize = 0;

    for (var entry : entries) {
      ColliderComponent colliderComponent = entry.c1();
      TransformComponent transformComponent = entry.c2();

      float newValue = Math.max(
        colliderComponent.getBoundingWidth(transformComponent.getScaleX()),
        colliderComponent.getBoundingHeight(transformComponent.getScaleY())
      );

      if (newValue > minSize) {
        minSize = Math.round(newValue);
      }
    }

    return minSize;
  }

  private int calculateAppropriateCellSizeForEntities(List<View2Entry<ColliderComponent, TransformComponent>> entries) {
    int maxWidthOrHeight = calculateMinSizeForEntities(entries) * 2;

    if (maxWidthOrHeight == 0) {
      return Integer.MAX_VALUE;
    }

    return maxWidthOrHeight;
  }

  public record BucketId(int x, int y) {
  }
}
