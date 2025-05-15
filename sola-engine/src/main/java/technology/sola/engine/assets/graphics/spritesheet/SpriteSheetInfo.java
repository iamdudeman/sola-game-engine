package technology.sola.engine.assets.graphics.spritesheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information about a spritesheet.
 *
 * @param spriteSheet the path to the spritesheet's image
 * @param sprites     the {@link SpriteInfo} definitions within the spritesheet
 */
public record SpriteSheetInfo(
  String spriteSheet,
  List<SpriteInfo> sprites
) {
  /**
   * Adds a new sprite to the sprite sheet and returns a new instance.
   *
   * @param newSpriteInfo the {@link SpriteInfo} of the new sprite
   * @return a new {@link SpriteSheetInfo} instance with the sprite added
   */
  public SpriteSheetInfo addSprite(SpriteInfo newSpriteInfo) {
    var newSprites = new ArrayList<>(sprites);

    newSprites.add(newSpriteInfo);

    return new SpriteSheetInfo(spriteSheet, newSprites);
  }

  /**
   * Updates a sprite by id and returns a new instance.
   *
   * @param id            the id of the sprite to update
   * @param newSpriteInfo the new {@link SpriteInfo} for the sprite
   * @return a new {@link SpriteSheetInfo} instance with the sprite updated
   */
  public SpriteSheetInfo updateSprite(String id, SpriteInfo newSpriteInfo) {
    var newSprites = sprites.stream()
      .map(spriteInfo -> spriteInfo.id().equals(id) ? newSpriteInfo : spriteInfo)
      .toList();

    return new SpriteSheetInfo(spriteSheet, newSprites);
  }

  /**
   * Removes a sprite by its id from this sprite sheet and returns a new instance.
   *
   * @param id the id of the sprite to remove
   * @return a new {@link SpriteSheetInfo} instance with the sprite removed
   */
  public SpriteSheetInfo removeSprite(String id) {
    var newSprites = sprites.stream()
      .filter(spriteInfo -> !spriteInfo.id().equals(id))
      .toList();

    return new SpriteSheetInfo(spriteSheet, newSprites);
  }
}
