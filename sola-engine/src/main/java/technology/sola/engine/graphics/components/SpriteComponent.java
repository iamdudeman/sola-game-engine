package technology.sola.engine.graphics.components;

import technology.sola.engine.assets.AssetPool;
import technology.sola.ecs.Component;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.sprite.SpriteSheet;

import java.io.Serial;

public class SpriteComponent implements Component {
  @Serial
  private static final long serialVersionUID = 5969143866970628671L;
  private SpriteKeyFrame spriteKeyFrame;

  public SpriteComponent(String spriteSheetId, String spriteId) {
    spriteKeyFrame = new SpriteKeyFrame(spriteSheetId, spriteId, 0);
  }

  public String getSpriteSheetId() {
    return spriteKeyFrame.getSpriteSheetId();
  }

  public String getSpriteId() {
    return spriteKeyFrame.getSpriteId();
  }

  public void setSpriteKeyFrame(SpriteKeyFrame spriteKeyFrame) {
    this.spriteKeyFrame = spriteKeyFrame;
  }

  public SolaImage getSprite(AssetPool<SpriteSheet> spriteSheetAssetPool) {
    return spriteKeyFrame.getSprite(spriteSheetAssetPool);
  }
}
