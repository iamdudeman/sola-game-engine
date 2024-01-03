package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

/**
 * SpriteEntityGraphicsModule is a {@link SolaEntityGraphicsModule} implementation for rendering {@link Entity} that have a
 * {@link TransformComponent} and {@link SpriteComponent}.
 */
public class SpriteEntityGraphicsModule extends SolaEntityGraphicsModule<View2Entry<SpriteComponent, TransformComponent>> {
  private final AssetLoader<SpriteSheet> spriteSheetAssetLoader;

  /**
   * Creates an instance of SpriteEntityGraphicsModule.
   *
   * @param spriteSheetAssetLoader the {@link AssetLoader} instance for {@link SpriteSheet}s
   */
  public SpriteEntityGraphicsModule(AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    this.spriteSheetAssetLoader = spriteSheetAssetLoader;
  }

  @Override
  public View<View2Entry<SpriteComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(SpriteComponent.class, TransformComponent.class);
  }

  @Override
  public void renderMethod(Renderer renderer, View2Entry<SpriteComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    SpriteComponent spriteComponent = viewEntry.c1();

    if (spriteComponent.getSpriteId() == null) {
      return;
    }

    spriteComponent.getSprite(spriteSheetAssetLoader).executeIfLoaded(sprite -> {
      SolaImage spriteImage = sprite;

      if (cameraModifiedEntityTransform.getScaleX() != 1 || cameraModifiedEntityTransform.getScaleY() != 1) {
        spriteImage = sprite.scale(cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY());
      }

      boolean hasBlendMode = viewEntry.entity().hasComponent(BlendModeComponent.class);

      if (!hasBlendMode) {
        renderer.setBlendFunction(BlendMode.MASK);
      }

      renderer.drawImage(spriteImage, cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY());
    });
  }
}
