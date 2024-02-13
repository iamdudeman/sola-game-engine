package technology.sola.engine.physics.system.collision;

import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.utils.SpatialHashMap;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.List;

public class SpacialHashMapCollisionDetectionBroadPhase implements CollisionDetectionBroadPhase {
  private SpatialHashMap spatialHashMap;
  private final Integer spatialHashMapCellSize;

  public SpacialHashMapCollisionDetectionBroadPhase(Integer spatialHashMapCellSize) {
    this.spatialHashMapCellSize = spatialHashMapCellSize;
  }

  @Override
  public void populate(List<View2Entry<ColliderComponent, TransformComponent>> views) {
    spatialHashMap = spatialHashMapCellSize == null ? new SpatialHashMap(views) : new SpatialHashMap(views, spatialHashMapCellSize);
  }

  @Override
  public List<View2Entry<ColliderComponent, TransformComponent>> query(View2Entry<ColliderComponent, TransformComponent> searchEntry) {
    return spatialHashMap.getNearbyViewEntries(searchEntry);
  }

  @Override
  public void renderDebug(Renderer renderer, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    int cellSize = spatialHashMap.getCellSize();

    for (SpatialHashMap.BucketId bucketId : spatialHashMap.getBucketIds()) {
      Vector2D topLeftPoint = new Vector2D(bucketId.x(), bucketId.y()).scalar(cellSize);

      var tempName = cameraTranslationTransform.multiply(topLeftPoint);
      var tempCell = cameraScaleTransform.multiply(cellSize, cellSize).x();

      renderer.drawRect(tempName.x(), tempName.y(), tempCell, tempCell, Color.GREEN);
    }
  }
}
