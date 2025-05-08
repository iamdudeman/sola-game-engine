package technology.sola.engine.assets.graphics.spritesheet;

/**
 * Contains information about a sprite within a spritesheet.
 *
 * @param id     the id of the sprite
 * @param x      the left most coordinate of the sprite
 * @param y      the top most coordinate of the sprite
 * @param width  the width of the sprite
 * @param height the height of the sprite
 */
public record SpriteInfo(
  String id,
  int x,
  int y,
  int width,
  int height
) {
}
