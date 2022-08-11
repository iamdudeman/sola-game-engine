package technology.sola.engine.assets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class BulkAssetLoader {
  private final AssetPoolProvider assetPoolProvider;

  public BulkAssetLoader(AssetPoolProvider assetPoolProvider) {
    this.assetPoolProvider = assetPoolProvider;
  }

  public BulkAssetHandle loadAll(BulkAssetDescription... bulkAssetDescriptions) {
    return loadAll(Arrays.asList(bulkAssetDescriptions));
  }

  public BulkAssetHandle loadAll(List<BulkAssetDescription> bulkAssetDescriptionList) {
    int totalToLoad = bulkAssetDescriptionList.size();
    int index = 0;
    Asset[] assets = new Asset[totalToLoad];
    AssetCounter assetCounter = new AssetCounter();
    BulkAssetHandle bulkAssetHandle = new BulkAssetHandle();

    for (BulkAssetDescription bulkAssetDescription : bulkAssetDescriptionList) {
      final int currentAssetIndex = index;

      assetPoolProvider
        .getAssetPool(bulkAssetDescription.assetClass())
        .addAndGetAsset(bulkAssetDescription.assetId(), bulkAssetDescription.path()).executeWhenLoaded(asset -> {
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

  public record BulkAssetDescription(Class<? extends Asset> assetClass, String assetId, String path) {
  }

  private static class AssetCounter {
    private int count = 0;

    synchronized void increment() {
      count++;
    }
  }
}
