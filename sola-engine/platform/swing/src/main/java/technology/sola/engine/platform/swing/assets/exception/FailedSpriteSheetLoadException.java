package technology.sola.engine.platform.swing.assets.exception;

import java.io.Serial;

public class FailedSpriteSheetLoadException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -374962772068241148L;

  public FailedSpriteSheetLoadException(String path) {
    super("Failed to load SpriteSheet with path [" + path + "]");
  }
}
