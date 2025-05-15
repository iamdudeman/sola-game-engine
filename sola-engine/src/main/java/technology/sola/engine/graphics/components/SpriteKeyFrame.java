package technology.sola.engine.graphics.components;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;

/**
 * SpriteKeyFrame holds information about how long a particular sprite should be rendered as part of an animation.
 */
public class SpriteKeyFrame {
  /**
   * This duration value informs the {@link technology.sola.engine.graphics.system.SpriteAnimatorSystem} to not tick
   * the duration of the sprite, so it will continue to render on this frame.
   */
  public static final long DURATION_FREEZE = -1L;

  private final String spriteSheetId;
  private final String spriteId;
  private final long duration;
  private final AssetHandle<SolaImage> cachedSpriteHandle = new AssetHandle<>();
  private boolean isRequested = false;

  /**
   * Creates a new sprite key frame from desired {@link SpriteSheet} id and sprite id. Defaults the duration to
   * {@link SpriteKeyFrame#DURATION_FREEZE}.
   *
   * @param spriteSheetId the SpriteSheet asset id
   * @param spriteId      the sprite id
   */
  public SpriteKeyFrame(String spriteSheetId, String spriteId) {
    this(spriteSheetId, spriteId, DURATION_FREEZE);
  }

  /**
   * Creates a new sprite key frame from desired {@link SpriteSheet} id, sprite id, and duration.
   *
   * @param spriteSheetId the SpriteSheet asset id
   * @param spriteId      the sprite id
   * @param duration      the duration of rendering for this frame in the animation sequence
   */
  public SpriteKeyFrame(String spriteSheetId, String spriteId, long duration) {
    this.spriteSheetId = spriteSheetId;
    this.spriteId = spriteId;
    this.duration = duration;
  }

  /**
   * Creates a new sprite key frame from desired {@link SolaImage}. Defaults the duration to
   * {@link SpriteKeyFrame#DURATION_FREEZE}.
   *
   * @param spriteImage the image of the sprite
   */
  public SpriteKeyFrame(SolaImage spriteImage) {
    this(spriteImage, DURATION_FREEZE);
  }

  /**
   * Creates a new sprite key frame from desired {@link SolaImage} and duration.
   *
   * @param spriteImage the image of the sprite
   * @param duration    the duration of rendering for this frame in the animation sequence
   */
  public SpriteKeyFrame(SolaImage spriteImage, long duration) {
    this.spriteSheetId = null;
    this.spriteId = null;
    this.duration = duration;
    isRequested = true;

    cachedSpriteHandle.setAsset(spriteImage);
  }

  /**
   * @return the {@link SpriteSheet} asset id
   */
  public String getSpriteSheetId() {
    return spriteSheetId;
  }

  /**
   * @return the sprite id in the {@link SpriteSheet}
   */
  public String getSpriteId() {
    return spriteId;
  }

  /**
   * @return the duration of this frame in the animation
   */
  public long getDuration() {
    return duration;
  }

  AssetHandle<SolaImage> getSprite(AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    if (!isRequested && cachedSpriteHandle.isLoading()) {
      isRequested = true;
      spriteSheetAssetLoader
        .get(spriteSheetId)
        .executeWhenLoaded(spriteSheet -> cachedSpriteHandle.setAsset(spriteSheet.getSprite(spriteId)));
    }

    return cachedSpriteHandle;
  }
}
