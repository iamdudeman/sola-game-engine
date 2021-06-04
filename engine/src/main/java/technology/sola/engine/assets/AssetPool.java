package technology.sola.engine.assets;

import java.util.HashMap;
import java.util.Map;

public abstract class AssetPool<T> {
  protected Map<String, T> cachedAssets = new HashMap<>();
  protected Map<String, String> assetIdToPathMap = new HashMap<>();

  public void addAssetId(String id, String path) {
    assetIdToPathMap.put(id, path);
  }

  public T addAndGetAsset(String id, String path) {
    addAssetId(id, path);

    return getAsset(id);
  }

  public T getAsset(String id) {
    if (!assetIdToPathMap.containsKey(id)) {
      // TODO specific exception
      throw new RuntimeException("id [" + id + "]does not exist");
    }

    return cachedAssets.computeIfAbsent(id, key -> loadAsset(assetIdToPathMap.get(key)));
  }

  public abstract Class<T> getAssetClass();

  protected abstract T loadAsset(String path);
}
