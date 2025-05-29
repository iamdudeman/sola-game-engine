package technology.sola.engine.platform.swing.assets.exception;

import org.jspecify.annotations.NullMarked;

import java.io.Serial;

/**
 * A {@link RuntimeException} that is thrown when a {@link technology.sola.engine.assets.graphics.SolaImage} fails
 * to load.
 */
@NullMarked
public class FailedSolaImageLoadException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 926132601237425305L;

  /**
   * Creates an instance of this exception.
   *
   * @param path the path of the failed to load SolaImage
   */
  public FailedSolaImageLoadException(String path) {
    super("Failed to load SolaImage with path [" + path + "]");
  }
}
