package technology.sola.engine.physics.utils;

import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.math.geometry.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The SpatialHashMap class is a spatial hashmap implementation that maps {@link technology.sola.ecs.Entity} into a
 * bucket with other entities based on its 2D coordinate. This helps reduce the number of collision checks that are
 * needed during broad phase collision detection.
 */
public class SpatialHashMap {
  private static final BucketId[] EMPTY_BUCKET_IDS = new BucketId[0];
  private final int cellSize;
  private final float inverseCellSize;
  private final Map<BucketId, List<View2Entry<ColliderComponent, TransformComponent>>> buckets = new HashMap<>();

  /**
   * Creates a SpatialHashMap with cellSize set to twice the max width/height of the largest {@link technology.sola.ecs.Entity}.
   *
   * @param viewEntries the list of {@link technology.sola.ecs.view.View2Entry} of {@link ColliderComponent} and {@link TransformComponent}
   */
  public SpatialHashMap(List<View2Entry<ColliderComponent, TransformComponent>> viewEntries) {
    this.cellSize = calculateAppropriateCellSizeForViewEntries(viewEntries);
    this.inverseCellSize = 1f / this.cellSize;

    viewEntries.forEach(this::registerViewEntry);
  }

  /**
   * Creates a SpatialHashMap with desired cell size. Ensure that the cellSize is greater than the max width or height
   * of the largest {@link technology.sola.ecs.Entity}.
   *
   * @param viewEntries the list of {@link technology.sola.ecs.view.View2Entry} of {@link ColliderComponent} and {@link TransformComponent}
   * @param cellSize    the cell size of the SpatialHashMap
   */
  public SpatialHashMap(List<View2Entry<ColliderComponent, TransformComponent>> viewEntries, int cellSize) {
    int minimumCellSize = calculateMinCellSizeForViewEntries(viewEntries);

    if (cellSize < minimumCellSize)
      throw new IllegalArgumentException("Cell size must be greater than largest object [" + minimumCellSize + "]");

    this.cellSize = cellSize;
    this.inverseCellSize = 1f / this.cellSize;
    viewEntries.forEach(this::registerViewEntry);
  }

  /**
   * @return the current cell size of this spatial hashmap
   */
  public int getCellSize() {
    return cellSize;
  }

  /**
   * Returns a list of {@link technology.sola.ecs.view.ViewEntry} containing an Entity that is in the same bucket as the
   * entity to check.
   *
   * @param viewEntry the view entry containing the entity to check
   * @return list of nearby entities
   */
  public List<View2Entry<ColliderComponent, TransformComponent>> getNearbyViewEntries(View2Entry<ColliderComponent, TransformComponent> viewEntry) {
    List<View2Entry<ColliderComponent, TransformComponent>> nearbyEntries = new ArrayList<>();

    for (BucketId bucketId : getBucketIdsForViewEntry(viewEntry)) {
      List<View2Entry<ColliderComponent, TransformComponent>> bucket = getOrCreateBucket(bucketId);

      for (var viewEntryInBucket : bucket) {
        if (viewEntry != viewEntryInBucket && !nearbyEntries.contains(viewEntryInBucket)) {
          nearbyEntries.add(viewEntryInBucket);
        }
      }
    }

    return nearbyEntries;
  }

  /**
   * @return the set of current {@link BucketId}s
   */
  public Set<BucketId> getBucketIds() {
    return buckets.keySet();
  }

  BucketId[] getBucketIdsForViewEntry(View2Entry<ColliderComponent, TransformComponent> entry) {
    TransformComponent transformComponent = entry.c2();
    ColliderComponent colliderComponent = entry.c1();

    if (transformComponent == null || colliderComponent == null) return EMPTY_BUCKET_IDS;

    Rectangle boundingBox = colliderComponent.getBoundingBox(transformComponent);

    float x = boundingBox.min().x();
    float y = boundingBox.min().y();
    float width = boundingBox.getWidth();
    float height = boundingBox.getHeight();

    return new BucketId[]{
      getIdForPoint(x, y),
      getIdForPoint(x + width, y),
      getIdForPoint(x, y + height),
      getIdForPoint(x + width, y + height),
    };
  }

  private void registerViewEntry(View2Entry<ColliderComponent, TransformComponent> viewEntry) {
    for (BucketId bucketId : getBucketIdsForViewEntry(viewEntry)) {
      List<View2Entry<ColliderComponent, TransformComponent>> bucket = getOrCreateBucket(bucketId);

      if (!bucket.contains(viewEntry)) {
        bucket.add(viewEntry);
      }
    }
  }

  private BucketId getIdForPoint(float x, float y) {
    return new BucketId((int) Math.floor(x * inverseCellSize), (int) Math.floor(y * inverseCellSize));
  }

  private List<View2Entry<ColliderComponent, TransformComponent>> getOrCreateBucket(BucketId bucketId) {
    return buckets.computeIfAbsent(bucketId, key -> new ArrayList<>());
  }

  private int calculateMinCellSizeForViewEntries(List<View2Entry<ColliderComponent, TransformComponent>> viewEntries) {
    int minSize = 0;

    for (var viewEntry : viewEntries) {
      ColliderComponent colliderComponent = viewEntry.c1();
      TransformComponent transformComponent = viewEntry.c2();
      var boundingBox = colliderComponent.getBoundingBox(transformComponent);

      float newValue = Math.max(
        boundingBox.getWidth(),
        boundingBox.getHeight()
      );

      if (newValue > minSize) {
        minSize = Math.round(newValue);
      }
    }

    return minSize;
  }

  private int calculateAppropriateCellSizeForViewEntries(List<View2Entry<ColliderComponent, TransformComponent>> viewEntries) {
    int maxWidthOrHeight = calculateMinCellSizeForViewEntries(viewEntries) * 2;

    if (maxWidthOrHeight == 0) {
      return Integer.MAX_VALUE;
    }

    return maxWidthOrHeight;
  }

  /**
   * BucketId holds the top, left coordinate that identifies a bucket.
   *
   * @param x the left coordinate value
   * @param y the y coordinate value
   */
  public record BucketId(int x, int y) {
  }
}
