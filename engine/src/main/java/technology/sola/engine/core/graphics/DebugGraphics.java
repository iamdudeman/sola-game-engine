package technology.sola.engine.core.graphics;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

class DebugGraphics {
  static void render(Renderer renderer, EcsSystemContainer ecsSystemContainer, TransformComponent cameraTransform) {
    CollisionDetectionSystem collisionDetectionSystem = ecsSystemContainer.get(CollisionDetectionSystem.class);

    int cellSize = collisionDetectionSystem.getSpacialHashMapCellSize();

    collisionDetectionSystem.getSpacialHashMapEntityBucketIterator()
      .forEachRemaining(bucketVector -> {
        Vector2D topLeftPoint = bucketVector.scalar(cellSize);

        TransformComponent useThis = GraphicsUtils.getTransformForAppliedCamera(
          new TransformComponent(topLeftPoint.x, topLeftPoint.y, cellSize, cellSize),
          cameraTransform
        );

        renderer.drawRect(useThis.getX(), useThis.getY(), useThis.getScaleX(), useThis.getScaleY(), Color.GREEN);
      });

    ecsSystemContainer.getWorld().getEntitiesWithComponents(ColliderComponent.class, TransformComponent.class)
      .forEach(entity -> {
        TransformComponent transformComponent = GraphicsUtils.getTransformForAppliedCamera(
          entity.getComponent(TransformComponent.class),
          cameraTransform
        );
        Vector2D transform = transformComponent.getTranslate();
        ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);

        if (ColliderComponent.ColliderType.CIRCLE.equals(colliderComponent.getColliderType())) {
          Circle circle = colliderComponent.asCircle(transformComponent);

          renderer.drawCircle(transform.x, transform.y, circle.getRadius(), Color.RED);
        } else {
          Rectangle rectangle = colliderComponent.asRectangle(transformComponent);

          renderer.drawRect(transform.x, transform.y, rectangle.getWidth(), rectangle.getHeight(), Color.RED);
        }
      });
  }
}