package technology.sola.engine.graphics.components;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.sprite.SpriteSheet;
import technology.sola.engine.graphics.sprite.exception.SpriteNotFoundException;

import java.io.Serial;
import java.io.Serializable;

public class SpriteKeyFrame implements Serializable {
  @Serial
  private static final long serialVersionUID = -1643322034379275616L;
  private final String spriteSheetId;
  private final String spriteId;
  private final long duration;
  private transient SolaImage cachedSprite;

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
