package technology.sola.engine.platform.javafx.assets.exception;

import java.io.Serial;

/**
 * A {@link RuntimeException} that is thrown when a {@link technology.sola.engine.assets.graphics.font.Font} fails
 * to load.
 */
public class FailedFontLoadException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 137323910988899574L;

  /**
   * Creates an instance of this exception.
   *
   * @param path the path to the failed to load Font
   */
  public FailedFontLoadException(String path) {
    super("Failed to load Font with path [" + path + "]");
  }
}
