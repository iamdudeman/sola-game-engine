package technology.sola.engine.assets.exception;

import java.io.Serial;

public class MissingAssetLoaderException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -989086575716192706L;

  public MissingAssetLoaderException(Class<?> assetClass) {
    super("AssetLoader for asset class [" + assetClass.getName() + "] does not exist");
  }
}
