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

  /**
   * @return true if the {@link Asset} for this handle has not yet been loaded
   */
  public boolean isLoading() {
    return asset == null;
  }

  public T getAsset() {
    if (isLoading()) {
      throw new RuntimeException("AssetHandle has not had its Asset loaded yet.");
    }

    return asset;
  }

  public synchronized void setAsset(T asset) {
    if (this.asset == null) {
      this.asset = asset;
      onLoadSubscribers.forEach(consumer -> consumer.accept(asset));
      onLoadSubscribers = null;
    }
  }

  /**
   * Executes the provided consumer asynchronously once the {@link Asset} for this handle has been loaded. This will
   * execute immediately if the asset is already loaded.
   *
   * @param consumer the function to execute if the asset is loaded
   */
  public void executeWhenLoaded(Consumer<T> consumer) {
    if (isLoading()) {
      onLoadSubscribers.add(consumer);
    } else {
      consumer.accept(asset);
    }
  }

  /**
   * Executes the provided consumer synchronously if the {@link Asset} for this handle has been loaded. Otherwise, it
   * does nothing.
   *
   * @param consumer the function to execute if the asset is loaded
   */
  public void executeIfLoaded(Consumer<T> consumer) {
    if (!isLoading()) {
      consumer.accept(asset);
    }
  }
}
