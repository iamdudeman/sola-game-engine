package technology.sola.engine.assets.exception;

import java.io.Serial;

/**
 * Exception thrown when a {@link technology.sola.engine.assets.Asset} is requested but has not been registered with
 * an {@link technology.sola.engine.assets.AssetLoader}.
 */
public class MissingAssetException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -9018242929183264139L;

  /**
   * Creates an instances of this exception.
   *
   * @param assetId the id of the asset requested
   */
  public MissingAssetException(String assetId) {
    super("Asset with id [" + assetId + "] does not exist");
  }
}
