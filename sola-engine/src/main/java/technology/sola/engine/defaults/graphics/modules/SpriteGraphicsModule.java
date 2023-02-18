package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View2;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.List;

/**
 * SpriteGraphicsModule is a {@link SolaGraphicsModule} implementation for rendering {@link Entity} that have a
 * {@link TransformComponent} and {@link SpriteComponent}.
 */
public class SpriteGraphicsModule extends SolaGraphicsModule<View2.View2Entry<SpriteComponent, TransformComponent>> {
  private final AssetLoader<SpriteSheet> spriteSheetAssetLoader;

  /**
   * Creates an instance of SpriteGraphicsModule.
   *
   * @param spriteSheetAssetLoader the {@link AssetLoader} instance for {@link SpriteSheet}s
   */
  public SpriteGraphicsModule(AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    this.spriteSheetAssetLoader = spriteSheetAssetLoader;
  }

  @Override
  public List<View2.View2Entry<SpriteComponent, TransformComponent>> getEntitiesToRender(World world) {
    return world.createView().of(SpriteComponent.class, TransformComponent.class).getEntries();
  }

  @Override
  public void renderMethod(Renderer renderer, View2.View2Entry<SpriteComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    SpriteComponent spriteComponent = viewEntry.c1();

    if (spriteComponent.getSpriteId() == null) {
      return;
    }

    spriteComponent.getSprite(spriteSheetAssetLoader).executeIfLoaded(sprite -> {
      SolaImage spriteImage = sprite;

      if (cameraModifiedEntityTransform.getScaleX() != 1 || cameraModifiedEntityTransform.getScaleY() != 1) {
        spriteImage = sprite.scale(cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY());
        renderer.setBlendMode(BlendMode.MASK);
      }

      renderer.drawImage(spriteImage, cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY());
    });
  }
}
