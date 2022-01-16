package technology.sola.engine.platform.javafx.assets.exception;

import java.io.Serial;

public class FailedSpriteSheetLoadException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -4618545414327330248L;

  public FailedSpriteSheetLoadException(String path) {
    super("Failed to load SpriteSheet with path [" + path + "]");
  }
}
