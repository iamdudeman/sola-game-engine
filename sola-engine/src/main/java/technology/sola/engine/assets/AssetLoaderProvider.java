package technology.sola.engine.assets;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.exception.MissingAssetLoaderException;

import java.util.HashMap;
import java.util.Map;

/**
 * The AssetLoaderProvider class holds instances of {@link AssetLoader}s for various {@link Asset} types.
 */
@NullMarked
public class AssetLoaderProvider {
  private final Map<Class<? extends Asset>, AssetLoader<?>> assetLoaderMap = new HashMap<>();

  /**
   * Adds an {@link AssetLoader} that can be retrieved and used later.
   *
   * @param assetLoader the {@code AssetLoader} to add
   */
  public void add(AssetLoader<?> assetLoader) {
    assetLoaderMap.put(assetLoader.getAssetClass(), assetLoader);
  }

  /**
   * Retrieves an {@link AssetLoader} for the desired {@link Asset} type via the asset's class.
   *
   * @param assetClass the class of asset to load
   * @param <T>        the type of Asset
   * @return the {@code AssetLoader} instance for the desired asset class
   * @throws MissingAssetLoaderException if there is no asset loader added for desired asset
   */
  @SuppressWarnings("unchecked")
  public <T extends Asset> AssetLoader<T> get(Class<T> assetClass) {
    AssetLoader<?> assetLoader = assetLoaderMap.get(assetClass);

    if (assetLoader == null) {
      throw new MissingAssetLoaderException(assetClass);
    }

    return (AssetLoader<T>) assetLoader;
  }
}
