package technology.sola.engine.assets;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * AssetHandle is a handle to an {@link Asset} that may or may not be loaded yet.
 *
 * @param <T> the type of the asset
 */
@NullMarked
public class AssetHandle<T extends Asset> {
  private final List<Consumer<T>> onLoadSubscribers;
  @Nullable
  private T asset;

  /**
   * Creates an empty AssetHandle with an empty list of onLoadSubscribers. Listeners will be notified when
   * {@link #setAsset(Asset)} is called with the loaded asset.
   */
  public AssetHandle() {
    onLoadSubscribers = new ArrayList<>();
  }

  /**
   * Creates an AssetHandle instance with a preloaded {@link Asset}.
   *
   * @param asset the preloaded asset
   */
  public AssetHandle(@Nullable T asset) {
    this();
    this.asset = asset;
  }

  /**
   * @return true if the {@link Asset} for this handle has not yet been loaded
   */
  public boolean isLoading() {
    return asset == null;
  }

  /**
   * Gets the loaded {@link Asset} immediately, but throws a runtime exception if it is not yet loaded.
   *
   * @return the loaded asset
   */
  @Nullable
  public T getAsset() {
    if (isLoading()) {
      throw new RuntimeException("AssetHandle has not had its Asset loaded yet.");
    }

    return asset;
  }

  /**
   * Sets the loaded {@link Asset} for this {@code AssetHandle}.
   *
   * @param asset the loaded asset
   */
  public synchronized void setAsset(T asset) {
    if (this.asset == null) {
      this.asset = asset;
      onLoadSubscribers.forEach(consumer -> consumer.accept(asset));
      onLoadSubscribers.clear();
    }
  }

  /**
   * Executes the provided consumer asynchronously once the {@link Asset} for this handle has been loaded. This will
   * execute immediately if the asset is already loaded.
   *
   * @param consumer the function to execute if the asset is loaded
   */
  public void executeWhenLoaded(Consumer<T> consumer) {
    if (isLoading() || asset == null) {
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
    if (!isLoading() && asset != null) {
      consumer.accept(asset);
    }
  }
}
