package technology.sola.engine.platform.swing.assets.exception;

import org.jspecify.annotations.NullMarked;

import java.io.Serial;

/**
 * A {@link RuntimeException} that is thrown when a {@link technology.sola.engine.assets.json.JsonElementAsset} fails
 * to load.
 */
@NullMarked
public class FailedJsonLoadException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 5390519056105259361L;

  /**
   * Creates an instance of this exception.
   *
   * @param path the path of the failed to load JsonElementAsset
   */
  public FailedJsonLoadException(String path) {
    super("Failed to load JsonElementAsset with path [" + path + "]");
  }
}
