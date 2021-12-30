package technology.sola.engine.graphics.components;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.ecs.Component;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.sprite.SpriteSheet;

public class SpriteComponent implements Component<SpriteComponent> {
  private static final long serialVersionUID = 5969143866970628671L;
  private SpriteKeyFrame spriteKeyFrame;

  public SpriteComponent(String spriteSheetId, String spriteId) {
    spriteKeyFrame = new SpriteKeyFrame(spriteSheetId, spriteId, 0);
  }

  @Override
  public SpriteComponent copy() {
    return new SpriteComponent(spriteKeyFrame.getSpriteSheetId(), spriteKeyFrame.getSpriteId());
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
