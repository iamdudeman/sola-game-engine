package technology.sola.engine.assets.graphics;

import technology.sola.engine.assets.Asset;

import java.util.HashMap;
import java.util.Map;

public class SpriteSheet implements Asset {
  private final SolaImage solaImage;
  private final Map<String, SolaImage> spriteIdMap = new HashMap<>();

  public SpriteSheet(SolaImage solaImage) {
    this.solaImage = solaImage;
  }

  public SolaImage addSpriteDefinition(String id, int x, int y, int width, int height) {
    SolaImage sprite = solaImage.getSubImage(x, y, width, height);

    spriteIdMap.put(id, solaImage.getSubImage(x, y, width, height));

    return sprite;
  }

  public SolaImage getSprite(String id) {
    return spriteIdMap.get(id);
  }
}
