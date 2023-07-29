package technology.sola.engine.assets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * BulkAssetLoader handles loading a collection of various {@link Asset}s proving a handle for the collection with
 * callbacks for when all the assets have finished loading.
 */
public class BulkAssetLoader {
  private final AssetLoaderProvider assetLoaderProvider;
  private final List<BulkAssetDescription> bulkAssetDescriptionList = new ArrayList<>();

  /**
   * Creates a BulkAssetLoader instance.
   *
   * @param assetLoaderProvider the {@link AssetLoaderProvider}
   */
  public BulkAssetLoader(AssetLoaderProvider assetLoaderProvider) {
    this.assetLoaderProvider = assetLoaderProvider;
  }

  /**
   * Adds an {@link Asset} to be loaded.
   *
   * @param assetClass the class of the asset
   * @param assetId    the id of the asset
   * @param path       the path to the asset
   * @return this
   */
  public BulkAssetLoader addAsset(Class<? extends Asset> assetClass, String assetId, String path) {
    bulkAssetDescriptionList.add(new BulkAssetDescription(assetClass, assetId, path));

    return this;
  }

  /**
   * Starts loading all the {@link Asset}s that have been added returning a {@link BulkAssetHandle} where callbacks
   * can be added to for being notified when loading finishes.
   *
   * @return the {@code BulkAssetHandle}
   */
  public BulkAssetHandle loadAll() {
    int totalToLoad = bulkAssetDescriptionList.size();
    int index = 0;
    Asset[] assets = new Asset[totalToLoad];
    AssetCounter assetCounter = new AssetCounter();
    BulkAssetHandle bulkAssetHandle = new BulkAssetHandle();

    for (BulkAssetDescription bulkAssetDescription : bulkAssetDescriptionList) {
      final int currentAssetIndex = index;

      assetLoaderProvider
        .get(bulkAssetDescription.assetClass())
        .getNewAsset(bulkAssetDescription.assetId(), bulkAssetDescription.path())
        .executeWhenLoaded(asset -> {
          assetCounter.increment();

          assets[currentAssetIndex] = asset;

          if (assetCounter.count >= totalToLoad) {
            bulkAssetHandle.complete(assets);
          }
        });

      index++;
    }

    return bulkAssetHandle;
  }

  /**
   * BulkAssetHandle holds the collection of {@link Asset}s that have been loaded by a {@link BulkAssetLoader}. These
   * are accessed via the onComplete method where callbacks can be added.
   */
  public static class BulkAssetHandle {
    private final List<Consumer<Asset[]>> onCompleteCallbacks = new ArrayList<>();
    private Asset[] assets;

    /**
     * Method called when bulk asset loading has completed. The array of {@link Asset}s that were loaded are passed
     * into the callback.
     *
     * @param callback called when loading finished with the loaded assets.
     */
    public void onComplete(Consumer<Asset[]> callback) {
      if (assets != null) {
        callback.accept(assets);
      } else {
        onCompleteCallbacks.add(callback);
      }
    }

    private void complete(Asset[] assets) {
      this.assets = assets;
      onCompleteCallbacks.forEach(callback -> callback.accept(assets));
    }

    private BulkAssetHandle() {
    }
  }

  private record BulkAssetDescription(Class<? extends Asset> assetClass, String assetId, String path) {
  }

  private static class AssetCounter {
    private int count = 0;

    synchronized void increment() {
      count++;
    }
  }
}
