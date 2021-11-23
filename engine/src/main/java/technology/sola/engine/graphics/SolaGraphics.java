package technology.sola.engine.graphics;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.sprite.SpriteAnimatorSystem;
import technology.sola.engine.graphics.sprite.SpriteSheet;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

public class SolaGraphics {
  private static final TransformComponent DEFAULT_CAMERA_TRANSFORM = new TransformComponent();
  private final EcsSystemContainer ecsSystemContainer;
  private final Renderer renderer;
  private final AssetPool<SpriteSheet> spriteSheetAssetPool;
  private boolean isRenderDebug = false;

  public SolaGraphics(EcsSystemContainer ecsSystemContainer, Renderer renderer, AssetPool<SpriteSheet> spriteSheetAssetPool) {
    this.ecsSystemContainer = ecsSystemContainer;
    this.renderer = renderer;
    this.spriteSheetAssetPool = spriteSheetAssetPool;
  }

  public void setRenderDebug(boolean renderDebug) {
    isRenderDebug = renderDebug;
  }

  // TODO improve performance of this method
  public Vector2D screenToWorldCoordinate(Vector2D screenCoordinate) {
    var cameraEntities = ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, CameraComponent.class);
    TransformComponent cameraTransform = cameraEntities.isEmpty()
      ? DEFAULT_CAMERA_TRANSFORM
      : cameraEntities.get(0).getComponent(TransformComponent.class);
    Matrix3D transform = Matrix3D.translate(-cameraTransform.getX(), -cameraTransform.getY())
      .multiply(Matrix3D.scale(cameraTransform.getScaleX(), cameraTransform.getScaleY()))
      .invert(); // TODO this invert is costly

    return transform.forward(screenCoordinate.x, screenCoordinate.y);
  }

  public void addEcsSystems() {
    ecsSystemContainer.add(new SpriteAnimatorSystem());
  }

  public void render() {
    var cameraEntities = ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, CameraComponent.class);

    TransformComponent cameraTransform = cameraEntities.isEmpty()
      ? DEFAULT_CAMERA_TRANSFORM
      : cameraEntities.get(0).getComponent(TransformComponent.class);

    // Draw rectangles
    ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, RectangleRendererComponent.class)
      .forEach(entity -> {
        LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

        if (layerComponent == null) {
          renderRectangle(entity, cameraTransform);
        } else {
          renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getPriority(), r -> renderRectangle(entity, cameraTransform));
        }
      });

    // Draw circles
    ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, CircleRendererComponent.class)
      .forEach(entity -> {
        LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

        if (layerComponent == null) {
          renderCircle(entity, cameraTransform);
        } else {
          renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getPriority(), r -> renderCircle(entity, cameraTransform));
        }
      });

    // Draw sprites
    ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, SpriteComponent.class)
      .forEach(entity -> {
        LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

        if (layerComponent == null) {
          renderSprite(entity, cameraTransform);
        } else {
          renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getPriority(), r -> renderSprite(entity, cameraTransform));
        }
      });

    if (isRenderDebug) {
      renderDebugPhysics(cameraTransform);
    }
  }

  private void renderRectangle(Entity entity, TransformComponent cameraTransform) {
    var transform = getTransformForAppliedCamera(entity.getComponent(TransformComponent.class), cameraTransform);
    var rectangleRenderer = entity.getComponent(RectangleRendererComponent.class);

    if (rectangleRenderer.getColor().hasAlpha()) {
      renderer.setRenderMode(RenderMode.ALPHA);
    }

    if (rectangleRenderer.isFilled()) {
      renderer.fillRect(transform.getX(), transform.getY(), transform.getScaleX(), transform.getScaleY(), rectangleRenderer.getColor());
    } else {
      renderer.drawRect(transform.getX(), transform.getY(), transform.getScaleX(), transform.getScaleY(), rectangleRenderer.getColor());
    }

    renderer.setRenderMode(RenderMode.NORMAL);
  }

  private void renderCircle(Entity entity, TransformComponent cameraTransform) {
    var transform = getTransformForAppliedCamera(entity.getComponent(TransformComponent.class), cameraTransform);
    var rectangleRenderer = entity.getComponent(CircleRendererComponent.class);
    float radius = Math.max(transform.getScaleX(), transform.getScaleY()) * 0.5f;

    if (rectangleRenderer.getColor().hasAlpha()) {
      renderer.setRenderMode(RenderMode.ALPHA);
    }

    if (rectangleRenderer.isFilled()) {
      renderer.fillCircle(transform.getX(), transform.getY(), radius, rectangleRenderer.getColor());
    } else {
      renderer.drawCircle(transform.getX(), transform.getY(), radius, rectangleRenderer.getColor());
    }

    renderer.setRenderMode(RenderMode.NORMAL);
  }

  private void renderSprite(Entity entity, TransformComponent cameraTransform) {
    TransformComponent transformComponent = getTransformForAppliedCamera(entity.getComponent(TransformComponent.class), cameraTransform);
    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

    SolaImage sprite = spriteComponent.getSprite(spriteSheetAssetPool);

    if (transformComponent.getScaleX() != 1 || transformComponent.getScaleY() != 1) {
      AffineTransform affineTransform = new AffineTransform()
        .translate(transformComponent.getX(), transformComponent.getY())
        .scale(transformComponent.getScaleX(), transformComponent.getScaleY());

      renderer.drawWithRenderModeMask(r -> renderer.drawImage(sprite, affineTransform));
    } else {
      renderer.drawImage(transformComponent.getX(), transformComponent.getY(), sprite);
    }
  }

  private void renderDebugPhysics(TransformComponent cameraTransform) {
    CollisionDetectionSystem collisionDetectionSystem = ecsSystemContainer.get(CollisionDetectionSystem.class);

    int cellSize = collisionDetectionSystem.getSpacialHashMapCellSize();

    collisionDetectionSystem.getSpacialHashMapEntityBucketIterator()
        .forEachRemaining(bucketVector -> {
          Vector2D topLeftPoint = bucketVector.scalar(cellSize);

          TransformComponent useThis = getTransformForAppliedCamera(
            new TransformComponent(topLeftPoint.x, topLeftPoint.y, cellSize, cellSize),
            cameraTransform
          );

          renderer.drawRect(useThis.getX(), useThis.getY(), useThis.getScaleX(), useThis.getScaleY(), Color.GREEN);
        });

    ecsSystemContainer.getWorld().getEntitiesWithComponents(ColliderComponent.class, TransformComponent.class)
      .forEach(entity -> {
        TransformComponent transformComponent = getTransformForAppliedCamera(
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

  // TODO this needs to be applied to CollisionSystem debug render as well
  private TransformComponent getTransformForAppliedCamera(TransformComponent entityTransform, TransformComponent cameraTransform) {
    Matrix3D cameraScaleTransform = Matrix3D.scale(cameraTransform.getScaleX(), cameraTransform.getScaleY());
    Vector2D entityScale = cameraScaleTransform.forward(entityTransform.getScaleX(), entityTransform.getScaleY());

    Matrix3D cameraTranslationTransform = Matrix3D.translate(-cameraTransform.getX(), -cameraTransform.getY())
      .multiply(cameraScaleTransform);
    Vector2D entityTranslation = cameraTranslationTransform.forward(entityTransform.getX(), entityTransform.getY());

    return new TransformComponent(
      entityTranslation.x, entityTranslation.y, entityScale.x, entityScale.y
    );
  }
}
