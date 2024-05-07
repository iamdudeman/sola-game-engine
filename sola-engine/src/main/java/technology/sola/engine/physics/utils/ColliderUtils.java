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
        float additionalOffsetX = 0;
        float additionalOffsetY = 0;
        float radius = width * 0.5f;

        if (width > height) {
          additionalOffsetY = height * 0.5f;
        } else if (width < height) {
          additionalOffsetX = width * 0.5f;
          radius = height * 0.5f;
        }

        entity.addComponent(ColliderComponent.circle(
          colliderComponent.getOffsetX() - additionalOffsetX,
          colliderComponent.getOffsetY() - additionalOffsetY,
          radius
        ));
      } else {
        throw new RuntimeException("Nope");
      }
    });

    return entity;
  }
}
