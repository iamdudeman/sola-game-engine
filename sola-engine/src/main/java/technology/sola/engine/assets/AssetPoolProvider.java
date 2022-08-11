package technology.sola.engine.assets;

import technology.sola.engine.assets.exception.MissingAssetPoolException;

import java.util.HashMap;
import java.util.Map;

public class AssetPoolProvider {
  private final Map<Class<?>, AssetPool<?>> assetPoolMap = new HashMap<>();

  public void add(AssetPool<?> assetPool) {
    assetPoolMap.put(assetPool.getAssetClass(), assetPool);
  }

  @SuppressWarnings("unchecked")
  public <T extends Asset> AssetPool<T> get(Class<T> assetClass) {
    AssetPool<?> assetPool = assetPoolMap.get(assetClass);

    if (assetPool == null) {
      throw new MissingAssetPoolException(assetClass);
    }

    return (AssetPool<T>) assetPool;
  }
}
