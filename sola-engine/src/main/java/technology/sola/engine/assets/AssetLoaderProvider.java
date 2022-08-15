package technology.sola.engine.assets;

import technology.sola.engine.assets.exception.MissingAssetLoaderException;

import java.util.HashMap;
import java.util.Map;

public class AssetLoaderProvider {
  private final Map<Class<?>, AssetLoader<?>> assetLoaderMap = new HashMap<>();

  public void add(AssetLoader<?> assetLoader) {
    assetLoaderMap.put(assetLoader.getAssetClass(), assetLoader);
  }

  @SuppressWarnings("unchecked")
  public <T extends Asset> AssetLoader<T> get(Class<T> assetClass) {
    AssetLoader<?> assetLoader = assetLoaderMap.get(assetClass);

    if (assetLoader == null) {
      throw new MissingAssetLoaderException(assetClass);
    }

    return (AssetLoader<T>) assetLoader;
  }
}
