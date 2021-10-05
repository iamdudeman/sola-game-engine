package technology.sola.engine.graphics.sprite;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.sprite.exception.SpriteNotFoundException;

public class SpriteKeyFrame {
  private final String spriteSheetId;
  private final String spriteId;
  private final long duration;
  private SolaImage cachedSprite;

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

  SolaImage getSprite(AssetPool<SpriteSheet> spriteSheetAssetPool) {
    if (cachedSprite == null) {
      SpriteSheet spriteSheet = spriteSheetAssetPool.getAsset(spriteSheetId);
      cachedSprite = spriteSheet.getSprite(spriteId);
    }

    if (cachedSprite == null) {
      throw new SpriteNotFoundException(spriteId);
    }

    return cachedSprite;
  }
}
