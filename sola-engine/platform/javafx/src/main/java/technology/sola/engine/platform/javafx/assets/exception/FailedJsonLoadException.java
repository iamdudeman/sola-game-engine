package technology.sola.engine.platform.javafx.assets.exception;

import java.io.Serial;

/**
 * A {@link RuntimeException} that is thrown when a {@link technology.sola.engine.assets.json.JsonElementAsset} fails
 * to load.
 */
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
