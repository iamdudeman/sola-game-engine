package technology.sola.engine.physics;

import technology.sola.ecs.Entity;
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
  private final Map<BucketId, List<Entity>> entityBuckets = new HashMap<>();

  /**
   * Creates a SpatialHashMap with cellSize set to twice the max width/height of the largest {@link Entity}.
   *
   * @param entities the list of entities being tracked by the SpatialHashMap
   */
  public SpatialHashMap(List<Entity> entities) {
    this.cellSize = calculateAppropriateCellSizeForEntities(entities);
    this.inverseCellSize = 1f / this.cellSize;

    entities.forEach(this::registerEntity);
  }

  /**
   * Creates a SpatialHashMap with desired cell size. Ensure that the cellSize is greater than the max width or height of the largest {@link Entity}.
   *
   * @param entities the list of entities being tracked by the SpatialHashMap
   * @param cellSize the cell size of the SpatialHashMap
   */
  public SpatialHashMap(List<Entity> entities, int cellSize) {
    int minimumCellSize = calculateMinSizeForEntities(entities);

    if (cellSize < minimumCellSize)
      throw new IllegalArgumentException("Cell size must be greater than largest object [" + minimumCellSize + "]");

    this.cellSize = cellSize;
    this.inverseCellSize = 1f / this.cellSize;
    entities.forEach(this::registerEntity);
  }

  public int getCellSize() {
    return cellSize;
  }

  public List<Entity> getNearbyEntities(Entity entity) {
    List<Entity> nearbyEntities = new ArrayList<>();

    for (BucketId bucketId : getBucketIdsForEntity(entity)) {
      List<Entity> bucket = getOrCreateBucket(bucketId);

      for (Entity entityInBucket : bucket) {
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

  BucketId[] getBucketIdsForEntity(Entity entity) {
    TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
    ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);

    if (transformComponent == null || colliderComponent == null) return EMPTY_BUCKET_IDS;

    float x = transformComponent.getX();
    float y = transformComponent.getY();
    float width = colliderComponent.getBoundingWidth(transformComponent.getScaleX());
    float height = colliderComponent.getBoundingHeight(transformComponent.getScaleY());

    return new BucketId[]{
      getIdForPoint(x, y),
      getIdForPoint(x + width, y),
      getIdForPoint(x, y + height),
      getIdForPoint(x + width, y + height),
    };
  }

  private void registerEntity(Entity entity) {
    for (BucketId bucketId : getBucketIdsForEntity(entity)) {
      List<Entity> entityBucket = getOrCreateBucket(bucketId);

      if (!entityBucket.contains(entity)) {
        entityBucket.add(entity);
      }
    }
  }

  private BucketId getIdForPoint(float x, float y) {
    return new BucketId((int) Math.floor(x * inverseCellSize), (int) Math.floor(y * inverseCellSize));
  }

  private List<Entity> getOrCreateBucket(BucketId bucketId) {
    return entityBuckets.computeIfAbsent(bucketId, key -> new ArrayList<>());
  }

  private int calculateMinSizeForEntities(List<Entity> entities) {
    int minSize = 0;

    for (Entity entity : entities) {
      ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);
      TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

      if (colliderComponent == null || transformComponent == null) continue;

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

  private int calculateAppropriateCellSizeForEntities(List<Entity> entities) {
    int maxWidthOrHeight = calculateMinSizeForEntities(entities) * 2;

    if (maxWidthOrHeight == 0) {
      return Integer.MAX_VALUE;
    }

    return maxWidthOrHeight;
  }

  public record BucketId(int x, int y) {
  }
}
