package technology.sola.engine.assets.exception;

public class MissingAssetPoolException extends RuntimeException {
  public MissingAssetPoolException(Class<?> assetClass) {
    super("AssetPool for asset class [" + assetClass.getName() + "] does not exist");
  }
}
