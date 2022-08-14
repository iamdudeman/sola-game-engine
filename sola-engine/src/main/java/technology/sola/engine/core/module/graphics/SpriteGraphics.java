package technology.sola.engine.core.module.graphics;

import technology.sola.ecs.Entity;
import technology.sola.ecs.SolaEcs;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.sprite.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

class SpriteGraphics {
  static void render(Renderer renderer, SolaEcs solaEcs, TransformComponent cameraTransform, AssetPool<SpriteSheet> spriteSheetAssetPool) {
    solaEcs.getWorld().findEntitiesWithComponents(TransformComponent.class, SpriteComponent.class)
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

    if (spriteComponent.getSpriteId() == null) {
      return;
    }

    spriteComponent.getSprite(spriteSheetAssetPool).executeIfLoaded(sprite -> {
      if (transformComponent.getScaleX() != 1 || transformComponent.getScaleY() != 1) {
        AffineTransform affineTransform = new AffineTransform()
          .translate(transformComponent.getX(), transformComponent.getY())
          .scale(transformComponent.getScaleX(), transformComponent.getScaleY());

        renderer.drawWithBlendMode(BlendMode.MASK, r -> renderer.drawImage(sprite, affineTransform));
      } else {
        renderer.drawImage(transformComponent.getX(), transformComponent.getY(), sprite);
      }
    });
  }
}
