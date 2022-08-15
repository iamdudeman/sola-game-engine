package technology.sola.engine.assets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BulkAssetLoader {
  private final AssetLoaderProvider assetLoaderProvider;
  private final List<BulkAssetDescription> bulkAssetDescriptionList = new ArrayList<>();

  public BulkAssetLoader(AssetLoaderProvider assetLoaderProvider) {
    this.assetLoaderProvider = assetLoaderProvider;
  }

  public BulkAssetLoader addAsset(Class<? extends Asset> assetClass, String assetId, String path) {
    bulkAssetDescriptionList.add(new BulkAssetDescription(assetClass, assetId, path));

    return this;
  }

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
        .getNewAsset(bulkAssetDescription.assetId(), bulkAssetDescription.path()).executeWhenLoaded(asset -> {
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

  public static class BulkAssetHandle {
    private final List<Consumer<Asset[]>> onCompleteCallbacks = new ArrayList<>();
    private Asset[] assets;

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
