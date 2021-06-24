package technology.sola.engine.assets;

import technology.sola.engine.exception.asset.MissingAssetException;

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
    String assetPath = assetIdToPathMap.get(id);

    if (assetPath == null) {
      throw new MissingAssetException(id);
    }

    return cachedAssets.computeIfAbsent(id, key -> loadAsset(assetPath));
  }

  public abstract Class<T> getAssetClass();

  protected abstract T loadAsset(String path);
}
