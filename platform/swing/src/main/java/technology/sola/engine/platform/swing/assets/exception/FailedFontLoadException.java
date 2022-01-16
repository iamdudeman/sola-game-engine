package technology.sola.engine.platform.swing.assets.exception;

import java.io.Serial;

public class FailedFontLoadException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -3731135571845397620L;

  public FailedFontLoadException(String path) {
    super("Failed to load Font with path [" + path + "]");
  }
}
