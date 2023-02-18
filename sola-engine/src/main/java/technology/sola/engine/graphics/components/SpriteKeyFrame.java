package technology.sola.engine.graphics.components;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;

public class SpriteKeyFrame {
  public static final long HOLD = -1L;

  private final String spriteSheetId;
  private final String spriteId;
  private final long duration;
  private final AssetHandle<SolaImage> cachedSpriteHandle = new AssetHandle<>();
  private boolean isRequested = false;

  public SpriteKeyFrame(String spriteSheetId, String spriteId) {
    this(spriteSheetId, spriteId, HOLD);
  }

  public SpriteKeyFrame(String spriteSheetId, String spriteId, long duration) {
    this.spriteSheetId = spriteSheetId;
    this.spriteId = spriteId;
    this.duration = duration;
  }

  public SpriteKeyFrame(SolaImage spriteImage) {
    this(spriteImage, HOLD);
  }

  public SpriteKeyFrame(SolaImage spriteImage, long duration) {
    this.spriteSheetId = null;
    this.spriteId = null;
    this.duration = duration;
    isRequested = true;

    cachedSpriteHandle.setAsset(spriteImage);
  }

  public String getSpriteSheetId() {
    return spriteSheetId;
  }

  public String getSpriteId() {
    return spriteId;
  }

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
