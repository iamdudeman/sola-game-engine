package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.SpatialHashMap;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.List;

public class DebugGraphicsModule extends SolaGraphicsModule {
  private final CollisionDetectionSystem collisionDetectionSystem;

  public DebugGraphicsModule() {
    collisionDetectionSystem = null;
  }

  public DebugGraphicsModule(CollisionDetectionSystem collisionDetectionSystem) {
    this.collisionDetectionSystem = collisionDetectionSystem;
  }

  @Override
  public List<Entity> getEntitiesToRender(World world) {
    return world.findEntitiesWithComponents(ColliderComponent.class, TransformComponent.class);
  }

  @Override
  public void render(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    super.render(renderer, world, cameraScaleTransform, cameraTranslationTransform);

    if (collisionDetectionSystem != null) {
      int cellSize = collisionDetectionSystem.getSpacialHashMapCellSize();

      for (SpatialHashMap.BucketId bucketId : collisionDetectionSystem.getSpacialHashMapEntityBuckets()) {
        Vector2D topLeftPoint = new Vector2D(bucketId.x(), bucketId.y()).scalar(cellSize);
        TransformComponent useThis = getTransformForAppliedCamera(
          new TransformComponent(topLeftPoint.x(), topLeftPoint.y(), cellSize, cellSize), cameraScaleTransform, cameraTranslationTransform
        );

        renderer.drawRect(useThis.getX(), useThis.getY(), useThis.getScaleX(), useThis.getScaleY(), Color.GREEN);
      }
    }
  }

  @Override
  public void renderMethod(Renderer renderer, Entity entity, TransformComponent entityTransform) {
    Vector2D transform = entityTransform.getTranslate();
    ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);

    if (ColliderComponent.ColliderType.CIRCLE.equals(colliderComponent.getColliderType())) {
      Circle circle = colliderComponent.asCircle(entityTransform);

      renderer.drawCircle(transform.x() + colliderComponent.getOffsetX(), transform.y() + colliderComponent.getOffsetY(), circle.radius(), Color.RED);
    } else {
      Rectangle rectangle = colliderComponent.asRectangle(entityTransform);

      renderer.drawRect(rectangle.min().x(), rectangle.min().y(), rectangle.getWidth(), rectangle.getHeight(), Color.RED);
    }
  }
}