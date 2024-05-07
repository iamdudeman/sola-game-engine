package technology.sola.engine.physics.utils;

import technology.sola.ecs.Entity;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.physics.component.ColliderComponent;

public class ColliderUtils {
  // todo a utility method like this maybe could be used to auto size collider based on sprite size
  public static Entity initializeColliderUsingSprite(Entity entity, AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    var colliderComponent = entity.getComponent(ColliderComponent.class);
    var spriteComponent = entity.getComponent(SpriteComponent.class);

    if (colliderComponent == null) {
      throw new RuntimeException("Required");
    }

    if (spriteComponent == null) {
      throw new RuntimeException("Required");
    }

    spriteComponent.addSpriteLoadedEvent(spriteSheetAssetLoader, sprite -> {
      var colliderType = colliderComponent.getColliderType();
      var width = sprite.getWidth();
      var height = sprite.getHeight();

      if (colliderType == ColliderComponent.ColliderType.AABB) {
        entity.addComponent(ColliderComponent.aabb(
          colliderComponent.getOffsetX(), colliderComponent.getOffsetY(),
          width, height
        ));
      } else if (colliderType == ColliderComponent.ColliderType.CIRCLE) {
        entity.addComponent(ColliderComponent.circle(
          colliderComponent.getOffsetX(), colliderComponent.getOffsetY(),
          Math.max(width, height) * 0.5f
        ));
      } else {
        throw new RuntimeException("Nope");
      }
    });

    return entity;
  }
}
