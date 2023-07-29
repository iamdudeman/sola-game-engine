package technology.sola.engine.assets.graphics;

import technology.sola.engine.assets.Asset;

import java.util.HashMap;
import java.util.Map;

/**
 * SpriteSheet is an {@link Asset} for a collection of sprites.
 */
public class SpriteSheet implements Asset {
  private final SolaImage solaImage;
  private final Map<String, SolaImage> spriteIdMap = new HashMap<>();

  /**
   * Creates a new SpriteSheet from a {@link SolaImage} source.
   *
   * @param solaImage the source image
   */
  public SpriteSheet(SolaImage solaImage) {
    this.solaImage = solaImage;
  }

  /**
   * Adds a sprite definition to the sprite sheet. This will be a sub-image of the source image that can be retrieved
   * layer by id.
   *
   * @param id     the id of the sprite
   * @param x      the left most coordinate of the sprite in the source image
   * @param y      the top most coordinate of the sprite in the source image
   * @param width  the width of the sprite in the source image
   * @param height the height of the sprite in the source image
   * @return the sprite's {@link SolaImage}
   */
  public SolaImage addSpriteDefinition(String id, int x, int y, int width, int height) {
    SolaImage sprite = solaImage.getSubImage(x, y, width, height);

    spriteIdMap.put(id, solaImage.getSubImage(x, y, width, height));

    return sprite;
  }

  /**
   * Gets a sprite's {@link SolaImage} by id.
   *
   * @param id the id of the sprite
   * @return the sprite's image
   */
  public SolaImage getSprite(String id) {
    return spriteIdMap.get(id);
  }
}
