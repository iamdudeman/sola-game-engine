package technology.sola.engine.graphics.components.sprite;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;

import java.io.Serial;
import java.io.Serializable;

public class SpriteKeyFrame implements Serializable {
  public static final long HOLD = -1L;

  @Serial
  private static final long serialVersionUID = -1643322034379275616L;
  private final String spriteSheetId;
  private final String spriteId;
  private final long duration;
  private final transient AssetHandle<SolaImage> cachedSprite = new AssetHandle<>();
  private transient boolean isRequested = false;

  public SpriteKeyFrame(String spriteSheetId, String spriteId) {
    this(spriteSheetId, spriteId, HOLD);
  }

  public SpriteKeyFrame(String spriteSheetId, String spriteId, long duration) {
    this.spriteSheetId = spriteSheetId;
    this.spriteId = spriteId;
    this.duration = duration;
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
    if (!isRequested && cachedSprite.isLoading()) {
      isRequested = true;
      spriteSheetAssetLoader
        .get(spriteSheetId)
        .executeWhenLoaded(spriteSheet -> cachedSprite.setAsset(spriteSheet.getSprite(spriteId)));
    }

    return cachedSprite;
  }
}