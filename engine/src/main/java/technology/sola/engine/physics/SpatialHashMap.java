package technology.sola.engine.physics;

import technology.sola.engine.ecs.Entity;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.PositionComponent;
import technology.sola.math.linear.Vector2D;

import java.util.*;
import java.util.stream.Collectors;

public class SpatialHashMap {
  private final int cellSize;
  private final float inverseCellSize;
  private final Map<Vector2D, List<Entity>> entityBuckets = new HashMap<>();

  /**
   * Creates a SpatialHashMap with cellSize set to twice the max width/height of the largest {@link Entity}.
   *
   * @param entities  the list of entities being tracked by the SpatialHashMap
   */
  public SpatialHashMap(List<Entity> entities) {
    this.cellSize = calculateAppropriateCellSizeForEntities(entities);
    this.inverseCellSize = 1f / this.cellSize;

    entities.forEach(this::registerEntity);
  }

  /**
   * Creates a SpatialHashMap with desired cell size. Ensure that the cellSize is greater than the max width or height of the largest {@link Entity}.
   *
   * @param entities  the list of entities being tracked by the SpatialHashMap
   * @param cellSize  the cell size of the SpatialHashMap
   */
  public SpatialHashMap(List<Entity> entities, int cellSize) {
    int minimumCellSize = calculateMinSizeForEntities(entities);

    if (cellSize < minimumCellSize) throw new IllegalArgumentException("Cell size must be greater than largest object [" + minimumCellSize + "]");

    this.cellSize = cellSize;
    this.inverseCellSize = 1f / this.cellSize;
    entities.forEach(this::registerEntity);
  }

  public int getCellSize() {
    return cellSize;
  }

  public List<Entity> getNearbyEntities(Entity entity) {
    return getBucketIdsForEntity(entity).stream()
      .map(this::getOrCreateBucket)
      .reduce(new ArrayList<>(), (nearbyEntities, entityBucket) -> {
        nearbyEntities.addAll(
          entityBucket.stream()
            .filter(entityInBucket -> entity != entityInBucket && !nearbyEntities.contains(entityInBucket))
            .collect(Collectors.toList())
        );

        return nearbyEntities;
      });
  }

  public Iterator<Vector2D> entityBucketIterator() {
    return entityBuckets.keySet().iterator();
  }

  List<Vector2D> getBucketIdsForEntity(Entity entity) {
    PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
    ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);

    if (positionComponent == null || colliderComponent == null) return Collections.emptyList();

    float x = positionComponent.getX();
    float y = positionComponent.getY();
    float width = colliderComponent.getBoundingWidth();
    float height = colliderComponent.getBoundingHeight();

    return List.of(
      getIdForPoint(x , y),
      getIdForPoint(x + width, y),
      getIdForPoint(x, y + height),
      getIdForPoint(x + width, y + height)
    );
  }

  private void registerEntity(Entity entity) {
    getBucketIdsForEntity(entity).forEach(bucketId -> {
      List<Entity> entityBucket = getOrCreateBucket(bucketId);
      if (!entityBucket.contains(entity)) {
        entityBucket.add(entity);
      }
    });
  }

  private Vector2D getIdForPoint(float x, float y) {
    return new Vector2D((float)Math.floor(x * inverseCellSize), (float)Math.floor(y * inverseCellSize));
  }

  private List<Entity> getOrCreateBucket(Vector2D bucketId) {
    return entityBuckets.computeIfAbsent(bucketId, key -> new ArrayList<>());
  }

  private int calculateMinSizeForEntities(List<Entity> entities) {
    return Math.round(
      entities.stream()
        .map(entity -> {
          ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);

          if (colliderComponent == null) return 0f;

          return Math.max(colliderComponent.getBoundingWidth(), colliderComponent.getBoundingHeight());
        })
        .max(Comparator.naturalOrder())
        .orElse(0.0f)
    );
  }

  private int calculateAppropriateCellSizeForEntities(List<Entity> entities) {
    int maxWidthOrHeight = calculateMinSizeForEntities(entities) * 2;

    if (maxWidthOrHeight == 0) {
      return Integer.MAX_VALUE;
    }

    return maxWidthOrHeight;
  }
}
