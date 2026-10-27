package technology.sola.engine.graphics.modules;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

/**
 * SpriteEntityGraphicsModule is a {@link SolaEntityGraphicsModule} implementation for rendering {@link Entity} that have a
 * {@link TransformComponent} and {@link SpriteComponent}.
 */
@NullMarked
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
  public void renderEntity(Renderer renderer, View2Entry<SpriteComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    SpriteComponent spriteComponent = viewEntry.c1();

    if (spriteComponent.getSpriteId() == null) {
      return;
    }

    var spriteAsset = spriteComponent.getSprite(spriteSheetAssetLoader);

    if (spriteAsset.isLoading()) {
      return;
    }

    var sprite = spriteAsset.getAsset();

    if (sprite == null) {
      return;
    }

    boolean hasBlendMode = viewEntry.entity().hasComponent(BlendModeComponent.class);
    var previousBlendFunction = renderer.getBlendFunction();

    if (!hasBlendMode) {
      renderer.setBlendFunction(BlendMode.MASK);
    }

    if (viewEntry.c1().isTiled()) {
      drawTiledSprite(sprite, cameraModifiedEntityTransform, renderer);
    } else {
      drawSingleSprite(sprite, cameraModifiedEntityTransform, renderer);
    }

    renderer.setBlendFunction(previousBlendFunction);
  }

  private void drawSingleSprite(SolaImage sprite, TransformComponent cameraModifiedEntityTransform, Renderer renderer) {
    SolaImage spriteImage = sprite;

    if (cameraModifiedEntityTransform.getScaleX() != 1 || cameraModifiedEntityTransform.getScaleY() != 1) {
      spriteImage = sprite.scale(cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY());
    }

    renderer.drawImage(spriteImage, cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY());
  }

  private void drawTiledSprite(SolaImage sprite, TransformComponent cameraModifiedEntityTransform, Renderer renderer) {
    int modX = cameraModifiedEntityTransform.getScaleX() > 0 ? 1 : -1;
    int modY = cameraModifiedEntityTransform.getScaleY() > 0 ? 1 : -1;
    float absX = Math.abs(cameraModifiedEntityTransform.getScaleX());
    float absY = Math.abs(cameraModifiedEntityTransform.getScaleY());

    renderer.setClamp(
      cameraModifiedEntityTransform.getX(),
      cameraModifiedEntityTransform.getY(),
      sprite.getWidth() * absX,
      sprite.getHeight() * absY
    );

    SolaImage spriteImage = modX != 1 || modY != 1
      ? sprite.scale(modX, modY)
      : sprite;

    for (int j = 0; j < absY; j++) {
      for (int i = 0; i < absX; i++) {
        renderer.drawImage(
          spriteImage,
          cameraModifiedEntityTransform.getX() + spriteImage.getWidth() * i,
          cameraModifiedEntityTransform.getY() + spriteImage.getHeight() * j
        );
      }
    }

    renderer.resetClamp();
  }
}
