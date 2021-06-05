package technology.sola.engine.exception.asset;

public class MissingAssetPoolException extends RuntimeException {
  public MissingAssetPoolException(Class<?> assetClass) {
    super("AssetPool for asset class [" + assetClass.getName() + "] does not exist");
  }
}
