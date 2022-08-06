package technology.sola.engine.assets;

import technology.sola.engine.assets.exception.MissingAssetException;

import java.util.HashMap;
import java.util.Map;

public abstract class AssetPool<T extends Asset> {
  protected Map<String, AssetHandle<T>> cachedAssets = new HashMap<>();
  protected Map<String, String> assetIdToPathMap = new HashMap<>();

  public AssetPool<T> addAssetId(String id, String path) {
    assetIdToPathMap.put(id, path);

    return this;
  }

  public AssetHandle<T> addAndGetAsset(String id, String path) {
    addAssetId(id, path);

    return getAsset(id);
  }

  public AssetHandle<T> getAsset(String id) {
    String assetPath = assetIdToPathMap.get(id);

    if (assetPath == null) {
      throw new MissingAssetException(id);
    }

    return cachedAssets.computeIfAbsent(id, key -> loadAsset(assetPath));
  }

  public abstract Class<T> getAssetClass();

  protected abstract AssetHandle<T> loadAsset(String path);
}
