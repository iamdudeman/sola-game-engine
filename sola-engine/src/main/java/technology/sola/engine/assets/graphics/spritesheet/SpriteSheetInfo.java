package technology.sola.engine.assets.graphics.spritesheet;

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
}
