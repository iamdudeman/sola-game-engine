package technology.sola.engine.core.graphics;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.sprite.SpriteSheet;

class SpriteGraphics {
  static void render(Renderer renderer, EcsSystemContainer ecsSystemContainer, TransformComponent cameraTransform, AssetPool<SpriteSheet> spriteSheetAssetPool) {
    ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, SpriteComponent.class)
      .forEach(entity -> {
        LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

        if (layerComponent == null) {
          renderSprite(renderer, entity, cameraTransform, spriteSheetAssetPool);
        } else {
          renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getPriority(), r -> renderSprite(renderer, entity, cameraTransform, spriteSheetAssetPool));
        }
      });
  }

  private static void renderSprite(Renderer renderer, Entity entity, TransformComponent cameraTransform, AssetPool<SpriteSheet> spriteSheetAssetPool) {
    TransformComponent transformComponent = GraphicsUtils.getTransformForAppliedCamera(entity.getComponent(TransformComponent.class), cameraTransform);
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