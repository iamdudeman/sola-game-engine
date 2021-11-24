package technology.sola.engine.platform.javafx.assets.exception;

public class FailedFontLoadException extends RuntimeException {
  public FailedFontLoadException(String path) {
    super("Failed to load Font with path [" + path + "]");
  }
}
