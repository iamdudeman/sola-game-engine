package technology.sola.engine.physics.utils;

import technology.sola.ecs.Entity;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.physics.component.ColliderComponent;

/**
 * ColliderUtils is a collection of utility methods for configuring {@link ColliderComponent}.
 */
public class ColliderUtils {
  /**
   * Auto sizes an {@link Entity}'s {@link ColliderComponent} using its {@link SpriteComponent}. This replaces the
   * initial collider using the sprite's dimensions once it has been loaded. If the initial collider is a circle then
   * additional offset will be added to the collider if the sprite's dimensions are not equal.
   *
   * @param entity                 the {@link Entity}
   * @param spriteSheetAssetLoader the {@link AssetLoader} for {@link SpriteSheet}
   * @return the entity
   */
  public static Entity autoSizeColliderToSprite(Entity entity, AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    var transformComponent = entity.getComponent(TransformComponent.class);
    var colliderComponent = entity.getComponent(ColliderComponent.class);
    var spriteComponent = entity.getComponent(SpriteComponent.class);

    if (colliderComponent == null) {
      throw new IllegalArgumentException("entity must have a ColliderComponent");
    }

    if (spriteComponent == null) {
      throw new IllegalStateException("entity must have a SpriteComponent");
    }

    spriteComponent.executeWhenLoaded(spriteSheetAssetLoader, sprite -> {
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
          additionalOffsetY = height * 0.5f * Math.max(transformComponent.getScaleX(), transformComponent.getScaleY());
        } else if (width < height) {
          additionalOffsetX = width * 0.5f * Math.max(transformComponent.getScaleX(), transformComponent.getScaleY());
          radius = height * 0.5f;
        }

        entity.addComponent(ColliderComponent.circle(
          colliderComponent.getOffsetX() - additionalOffsetX,
          colliderComponent.getOffsetY() - additionalOffsetY,
          radius
        ));
      } else {
        throw new IllegalStateException("Unsupported collider type: " + colliderType);
      }
    });

    return entity;
  }

  private ColliderUtils() {
  }
}
