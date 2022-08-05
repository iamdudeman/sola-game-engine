package technology.sola.engine.assets;

import java.util.function.Consumer;

public class AssetHandle<T extends Asset> {
  private T asset;

  public AssetHandle() {
  }

  public AssetHandle(T asset) {
    this.asset = asset;
  }

  public boolean isLoading() {
    return asset == null;
  }

  public T getAsset() {
    return asset;
  }

  public void setAsset(T asset) {
    this.asset = asset;
  }

  public void executeWhenLoaded(Consumer<T> consumer) {
    if (!isLoading()) {
      consumer.accept(asset);
    }
  }
}
