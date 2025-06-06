package technology.sola.engine.graphics.components;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Component;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;

import java.util.function.Consumer;

/**
 * SpriteComponent is a {@link Component} that contains data for rendering 2d sprites for an
 * {@link technology.sola.ecs.Entity}.
 */
@NullMarked
public class SpriteComponent implements Component {
  private SpriteKeyFrame spriteKeyFrame;

  /**
   * Creates a SpriteComponent for a sprite sheet and sprite id.
   *
   * @param spriteSheetId the id for the {@link SpriteSheet} asset
   * @param spriteId      the id for the sprite
   */
  public SpriteComponent(String spriteSheetId, String spriteId) {
    spriteKeyFrame = new SpriteKeyFrame(spriteSheetId, spriteId);
  }

  /**
   * Creates a SpriteComponent from a {@link SpriteKeyFrame}.
   *
   * @param spriteKeyFrame the sprite's key frame
   */
  public SpriteComponent(SpriteKeyFrame spriteKeyFrame) {
    this.spriteKeyFrame = spriteKeyFrame;
  }

  /**
   * Creates a SpriteComponent from a {@link SolaImage}.
   *
   * @param solaImage the image for the sprite
   */
  public SpriteComponent(SolaImage solaImage) {
    this.spriteKeyFrame = new SpriteKeyFrame(solaImage);
  }

  /**
   * @return the id of the {@link SpriteSheet} this sprite is in
   */
  @Nullable
  public String getSpriteSheetId() {
    return spriteKeyFrame.getSpriteSheetId();
  }

  /**
   * @return the id for the sprite
   */
  @Nullable
  public String getSpriteId() {
    return spriteKeyFrame.getSpriteId();
  }

  /**
   * Updates the {@link SpriteKeyFrame}.
   *
   * @param spriteKeyFrame the new {@code SpriteKeyFrame}
   */
  public void setSpriteKeyFrame(SpriteKeyFrame spriteKeyFrame) {
    this.spriteKeyFrame = spriteKeyFrame;
  }

  /**
   * Returns an {@link AssetHandle} for the {@link SolaImage} of the sprite.
   *
   * @param spriteSheetAssetLoader the {@link AssetLoader} for {@link SpriteSheet}s
   * @return the {@code AssetHandle} for the sprite
   */
  public AssetHandle<SolaImage> getSprite(AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    return spriteKeyFrame.getSprite(spriteSheetAssetLoader);
  }

  /**
   * Executes the provided consumer asynchronously once the sprite for this {@link SpriteComponent} has been loaded.
   * This will execute immediately if the sprite is already loaded.
   *
   * @param spriteSheetAssetLoader the {@link AssetLoader} for {@link SpriteSheet}s
   * @param onSpriteLoaded         the function to execute if the asset is loaded
   */
  public void executeWhenLoaded(AssetLoader<SpriteSheet> spriteSheetAssetLoader, Consumer<SolaImage> onSpriteLoaded) {
    spriteKeyFrame.getSprite(spriteSheetAssetLoader)
      .executeWhenLoaded(onSpriteLoaded);
  }
}
