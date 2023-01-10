package technology.sola.engine.core.module.graphics;

import technology.sola.ecs.Entity;
import technology.sola.ecs.SolaEcs;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

class SpriteGraphics {
  static void render(Renderer renderer, SolaEcs solaEcs, TransformComponent cameraTransform, AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    for (Entity entity : solaEcs.getWorld().findEntitiesWithComponents(TransformComponent.class, SpriteComponent.class)) {
      LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

      if (layerComponent == null) {
        renderSprite(renderer, entity, cameraTransform, spriteSheetAssetLoader);
      } else {
        renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getOrder(), r -> renderSprite(renderer, entity, cameraTransform, spriteSheetAssetLoader));
      }
    }
  }

  private static void renderSprite(Renderer renderer, Entity entity, TransformComponent cameraTransform, AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    TransformComponent transformComponent = GraphicsUtils.getTransformForAppliedCamera(entity.getComponent(TransformComponent.class), cameraTransform);
    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

    if (spriteComponent.getSpriteId() == null) {
      return;
    }

    spriteComponent.getSprite(spriteSheetAssetLoader).executeIfLoaded(sprite -> {
      BlendModeComponent blendModeComponent = entity.getComponent(BlendModeComponent.class);
      BlendMode previousBlendMode = renderer.getBlendMode();
      SolaImage spriteImage = sprite;
      BlendMode blendMode = blendModeComponent == null ? previousBlendMode : blendModeComponent.getRenderMode();

      if (transformComponent.getScaleX() != 1 || transformComponent.getScaleY() != 1) {
        spriteImage = sprite.scale(transformComponent.getScaleX(), transformComponent.getScaleY());
        blendMode = BlendMode.MASK;
      }

      renderer.setBlendMode(blendMode);
      renderer.drawImage(spriteImage, transformComponent.getX(), transformComponent.getY());
      renderer.setBlendMode(previousBlendMode);
    });
  }
}
