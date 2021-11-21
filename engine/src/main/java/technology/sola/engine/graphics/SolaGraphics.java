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

public class SolaGraphics {
  private static final TransformComponent DEFAULT_CAMERA_TRANSFORM = new TransformComponent();
  private final EcsSystemContainer ecsSystemContainer;
  private final Renderer renderer;
  private final AssetPool<SpriteSheet> spriteSheetAssetPool;

  public SolaGraphics(EcsSystemContainer ecsSystemContainer, Renderer renderer, AssetPool<SpriteSheet> spriteSheetAssetPool) {
    this.ecsSystemContainer = ecsSystemContainer;
    this.renderer = renderer;
    this.spriteSheetAssetPool = spriteSheetAssetPool;
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
  }

  private void renderRectangle(Entity entity, TransformComponent cameraTransform) {
    var transform = entity.getComponent(TransformComponent.class).apply(cameraTransform);
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
    var transform = entity.getComponent(TransformComponent.class).apply(cameraTransform);
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
    TransformComponent transformComponent = entity.getComponent(TransformComponent.class).apply(cameraTransform);
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
}
