package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.List;

public class SpriteGraphicsModule extends SolaGraphicsModule {
  private final AssetLoader<SpriteSheet> spriteSheetAssetLoader;

  public SpriteGraphicsModule(AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    this.spriteSheetAssetLoader = spriteSheetAssetLoader;
  }

  @Override
  public List<Entity> getEntitiesToRender(World world) {
    return world.findEntitiesWithComponents(TransformComponent.class, SpriteComponent.class);
  }

  @Override
  public void renderMethod(Renderer renderer, Entity entity, TransformComponent entityTransform) {
    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

    if (spriteComponent.getSpriteId() == null) {
      return;
    }

    spriteComponent.getSprite(spriteSheetAssetLoader).executeIfLoaded(sprite -> {
      SolaImage spriteImage = sprite;

      if (entityTransform.getScaleX() != 1 || entityTransform.getScaleY() != 1) {
        spriteImage = sprite.scale(entityTransform.getScaleX(), entityTransform.getScaleY());
        renderer.setBlendMode(BlendMode.MASK);
      }

      renderer.drawImage(spriteImage, entityTransform.getX(), entityTransform.getY());
    });
  }
}
