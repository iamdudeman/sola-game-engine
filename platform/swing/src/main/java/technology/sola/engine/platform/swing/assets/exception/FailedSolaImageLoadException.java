package technology.sola.engine.platform.swing.assets.exception;

public class FailedSolaImageLoadException extends RuntimeException {
  public FailedSolaImageLoadException(String path) {
    super("Failed to load SolaImage with path [" + path + "]");
  }
}
