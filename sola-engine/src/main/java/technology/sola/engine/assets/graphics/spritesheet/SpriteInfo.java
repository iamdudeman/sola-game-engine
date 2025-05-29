package technology.sola.engine.assets.graphics.spritesheet;

import org.jspecify.annotations.NullMarked;

/**
 * Contains information about a sprite within a spritesheet.
 *
 * @param id     the id of the sprite
 * @param x      the left most coordinate of the sprite
 * @param y      the top most coordinate of the sprite
 * @param width  the width of the sprite
 * @param height the height of the sprite
 */
@NullMarked
public record SpriteInfo(
  String id,
  int x,
  int y,
  int width,
  int height
) {
  /**
   * Checks to see if this sprite info has the same boundaries that another sprite does.
   *
   * @param other the other {@link SpriteInfo} to compare bounds with
   * @return true if they have the same bounds
   */
  public boolean hasSameBounds(final SpriteInfo other) {
    return x == other.x && y == other.y && width == other.width && height == other.height;
  }
}
