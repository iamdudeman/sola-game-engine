package technology.sola.engine.assets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AssetHandle<T extends Asset> {
  private T asset;
  private List<Consumer<T>> onLoadSubscribers;

  public AssetHandle() {
    onLoadSubscribers = new ArrayList<>();
  }

  public AssetHandle(T asset) {
    this.asset = asset;
  }

  public boolean isLoading() {
    return asset == null;
  }

  public T getAsset() {
    if (isLoading()) {
      throw new RuntimeException("AssetHandle has not had its Asset loaded yet.");
    }

    return asset;
  }

  public void setAsset(T asset) {
    if (this.asset == null) {
      this.asset = asset;
      onLoadSubscribers.forEach(consumer -> consumer.accept(asset));
      onLoadSubscribers = null;
    }
  }

  public void executeWhenLoaded(Consumer<T> consumer) {
    if (isLoading()) {
      onLoadSubscribers.add(consumer);
    } else {
      consumer.accept(asset);
    }
  }

  public void executeIfLoaded(Consumer<T> consumer) {
    if (!isLoading()) {
      consumer.accept(asset);
    }
  }
}
