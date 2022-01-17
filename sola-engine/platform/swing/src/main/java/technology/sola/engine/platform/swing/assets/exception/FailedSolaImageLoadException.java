package technology.sola.engine.platform.swing.assets.exception;

import java.io.Serial;

public class FailedSolaImageLoadException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 926132601237425305L;

  public FailedSolaImageLoadException(String path) {
    super("Failed to load SolaImage with path [" + path + "]");
  }
}
