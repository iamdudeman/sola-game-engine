package technology.sola.engine.assets.exception;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.Asset;

import java.io.Serial;

/**
 * Exception that is thrown when an {@link technology.sola.engine.assets.AssetLoader} is requested but has no
 * implementation provided.
 */
@NullMarked
public class MissingAssetLoaderException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -989086575716192706L;

  /**
   * Creates an instance of this exception.
   *
   * @param assetClass the class of {@link technology.sola.engine.assets.Asset} that was requested
   */
  public MissingAssetLoaderException(Class<? extends Asset> assetClass) {
    super("AssetLoader for asset class [" + assetClass.getName() + "] does not exist");
  }
}
