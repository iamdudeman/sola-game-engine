package technology.sola.engine.platform.swing.assets.exception;

import java.io.Serial;

/**
 * A {@link RuntimeException} that is thrown when a {@link technology.sola.engine.assets.graphics.SpriteSheet} fails
 * to load.
 */
public class FailedSpriteSheetLoadException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -374962772068241148L;

  /**
   * Creates an instance of this exception.
   *
   * @param path the path of the failed to load SpriteSheet
   */
  public FailedSpriteSheetLoadException(String path) {
    super("Failed to load SpriteSheet with path [" + path + "]");
  }
}
