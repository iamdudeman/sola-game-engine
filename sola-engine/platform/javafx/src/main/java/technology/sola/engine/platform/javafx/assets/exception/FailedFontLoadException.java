package technology.sola.engine.platform.javafx.assets.exception;

import java.io.Serial;

public class FailedFontLoadException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 137323910988899574L;

  public FailedFontLoadException(String path) {
    super("Failed to load Font with path [" + path + "]");
  }
}
