package technology.sola.engine.exception.asset;

public class MissingAssetException extends RuntimeException {
  public MissingAssetException(String assetId) {
    super("Asset with id [" + assetId + "] does not exist");
  }
}
