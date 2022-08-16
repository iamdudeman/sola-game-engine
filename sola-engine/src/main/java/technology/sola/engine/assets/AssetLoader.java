package technology.sola.engine.assets;

import technology.sola.engine.assets.exception.MissingAssetException;

import java.util.HashMap;
import java.util.Map;

public abstract class AssetLoader<T extends Asset> {
  protected Map<String, AssetHandle<T>> cachedAssets = new HashMap<>();
  protected Map<String, String> assetIdToPathMap = new HashMap<>();

  /**
   * Adds an asset id to path mapping but does not populate the cache until the asset is requested via {@link AssetLoader#get(String)}
   * @param id
   * @param path
   * @return
   */
  public AssetLoader<T> addAssetMapping(String id, String path) {
    assetIdToPathMap.put(id, path);

    return this;
  }

  public boolean hasAssetMapping(String id) {
    return assetIdToPathMap.containsKey(id);
  }

  public AssetLoader<T> addAsset(String id, T asset) {
    cachedAssets.put(id, new AssetHandle<>(asset));
    assetIdToPathMap.put(id, "manual");

    return this;
  }

  /**
   * Returns an {@link AssetHandle} for the asset requested or throws an exception if id to path mapping not found.
   * @param id
   * @return
   */
  public AssetHandle<T> get(String id) {
    String assetPath = assetIdToPathMap.get(id);

    if (assetPath == null) {
      throw new MissingAssetException(id);
    }

    return cachedAssets.computeIfAbsent(id, key -> loadAsset(assetPath));
  }

  /**
   * Adds an asset id to path mapping and then returns an {@link AssetHandle} to the asset.
   * @param id
   * @param path
   * @return
   */
  public AssetHandle<T> getNewAsset(String id, String path) {
    addAssetMapping(id, path);

    return get(id);
  }

  public abstract Class<T> getAssetClass();

  protected abstract AssetHandle<T> loadAsset(String path);
}
