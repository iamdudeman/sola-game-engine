package technology.sola.engine.assets;

import technology.sola.engine.assets.exception.MissingAssetException;

import java.util.HashMap;
import java.util.Map;

/**
 * The AssetLoader class contains functionality for loading an {@link Asset} type. These are implemented for each
 * {@link technology.sola.engine.core.SolaPlatform} and {@link Asset} combination.
 *
 * @param <T> the {@link Asset} type
 */
public abstract class AssetLoader<T extends Asset> {
  private final Map<String, AssetHandle<T>> cachedAssets = new HashMap<>();
  private final Map<String, String> assetIdToPathMap = new HashMap<>();

  /**
   * Adds an asset id to path mapping but does not populate the cache until the asset is requested via {@link AssetLoader#get(String)}
   *
   * @param id   id of the asset
   * @param path path to the asset
   * @return this
   */
  public AssetLoader<T> addAssetMapping(String id, String path) {
    assetIdToPathMap.put(id, path);

    return this;
  }

  /**
   * Checks to see if an asset mapping has already been added to this loader.
   *
   * @param id the id for the asset mapping
   * @return true if the asset mapping has already been added
   */
  public boolean hasAssetMapping(String id) {
    return assetIdToPathMap.containsKey(id);
  }

  /**
   * Manually add an {@link Asset} to this loader.
   *
   * @param id    the id for the {@code Asset}
   * @param asset the {@code Asset}
   * @return this loader
   */
  public AssetLoader<T> addAsset(String id, T asset) {
    cachedAssets.put(id, new AssetHandle<>(asset));
    assetIdToPathMap.put(id, "");

    return this;
  }

  /**
   * Returns an {@link AssetHandle} for the asset requested or throws an exception if id to path mapping not found.
   *
   * @param id id of the asset
   * @return handle for the asset
   */
  public AssetHandle<T> get(String id) {
    String assetPath = assetIdToPathMap.get(id);

    if (assetPath == null) {
      throw new MissingAssetException(id);
    }

    AssetHandle<T> cachedAsset = cachedAssets.get(id);

    if (cachedAsset == null) {
      cachedAsset = loadAsset(assetPath);
      cachedAssets.put(id, cachedAsset);
    }

    return cachedAsset;
  }

  /**
   * Adds an asset id to path mapping and then returns an {@link AssetHandle} to the asset.
   *
   * @param id   id of the asset
   * @param path path to the asset
   * @return handle for the asset
   */
  public AssetHandle<T> getNewAsset(String id, String path) {
    AssetHandle<T> newAsset = loadAsset(path);

    addAssetMapping(id, path);
    cachedAssets.put(id, newAsset);

    return newAsset;
  }

  /**
   * @return The {@link Class} of the {@link Asset}
   */
  public abstract Class<T> getAssetClass();

  /**
   * Returns an {@link AssetHandle} for the specified path and beings loading the {@link Asset}.
   *
   * @param path the path to the asset
   * @return the {@code AssetHandle} for the path
   */
  protected abstract AssetHandle<T> loadAsset(String path);
}
