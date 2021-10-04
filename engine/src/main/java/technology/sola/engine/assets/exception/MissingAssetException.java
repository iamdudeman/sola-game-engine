package technology.sola.engine.assets.exception;

public class MissingAssetException extends RuntimeException {
  public MissingAssetException(String assetId) {
    super("Asset with id [" + assetId + "] does not exist");
  }
}
