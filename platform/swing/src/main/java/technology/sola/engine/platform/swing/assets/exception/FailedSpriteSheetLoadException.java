package technology.sola.engine.platform.swing.assets.exception;

public class FailedSpriteSheetLoadException extends RuntimeException {
  public FailedSpriteSheetLoadException(String path) {
    super("Failed to load SpriteSheet with path [" + path + "]");
  }
}
