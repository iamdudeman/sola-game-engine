package technology.sola.engine.graphics.components.sprite;

import technology.sola.ecs.Component;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;

import java.io.Serial;

public class SpriteComponent implements Component {
  @Serial
  private static final long serialVersionUID = 5969143866970628671L;
  private SpriteKeyFrame spriteKeyFrame;

  public SpriteComponent(String spriteSheetId, String spriteId) {
    spriteKeyFrame = new SpriteKeyFrame(spriteSheetId, spriteId);
  }

  public SpriteComponent(SpriteKeyFrame spriteKeyFrame) {
    this.spriteKeyFrame = spriteKeyFrame;
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

  public AssetHandle<SolaImage> getSprite(AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    return spriteKeyFrame.getSprite(spriteSheetAssetLoader);
  }
}
