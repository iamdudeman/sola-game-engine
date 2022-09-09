package technology.sola.engine.core.module.graphics;

import technology.sola.ecs.Entity;
import technology.sola.ecs.SolaEcs;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.SpatialHashMap;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

class DebugGraphics {
  static void render(Renderer renderer, SolaEcs solaEcs, TransformComponent cameraTransform) {
    CollisionDetectionSystem collisionDetectionSystem = solaEcs.getSystem(CollisionDetectionSystem.class);

    int cellSize = collisionDetectionSystem.getSpacialHashMapCellSize();

    for (SpatialHashMap.BucketId bucketId : collisionDetectionSystem.getSpacialHashMapEntityBuckets()) {
      Vector2D topLeftPoint = new Vector2D(bucketId.x(), bucketId.y()).scalar(cellSize);

      TransformComponent useThis = GraphicsUtils.getTransformForAppliedCamera(
        new TransformComponent(topLeftPoint.x(), topLeftPoint.y(), cellSize, cellSize),
        cameraTransform
      );

      renderer.drawRect(useThis.getX(), useThis.getY(), useThis.getScaleX(), useThis.getScaleY(), Color.GREEN);
    }

    for (Entity entity : solaEcs.getWorld().findEntitiesWithComponents(ColliderComponent.class, TransformComponent.class)) {
      TransformComponent transformComponent = GraphicsUtils.getTransformForAppliedCamera(
        entity.getComponent(TransformComponent.class),
        cameraTransform
      );
      Vector2D transform = transformComponent.getTranslate();
      ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);

      if (ColliderComponent.ColliderType.CIRCLE.equals(colliderComponent.getColliderType())) {
        Circle circle = colliderComponent.asCircle(transformComponent);

        renderer.drawCircle(transform.x(), transform.y(), circle.getRadius(), Color.RED);
      } else {
        Rectangle rectangle = colliderComponent.asRectangle(transformComponent);

        renderer.drawRect(transform.x(), transform.y(), rectangle.getWidth(), rectangle.getHeight(), Color.RED);
      }
    }
  }
}
