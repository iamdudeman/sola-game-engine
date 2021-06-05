package technology.sola.engine.assets;

import technology.sola.engine.exception.asset.MissingAssetPoolException;

import java.util.HashMap;
import java.util.Map;

public class AssetPoolProvider {
  private final Map<Class<?>, AbstractAssetPool<?>> assetPoolMap = new HashMap<>();

  public void addAssetPool(AbstractAssetPool<?> assetPool) {
    assetPoolMap.put(assetPool.getAssetClass(), assetPool);
  }

  @SuppressWarnings("unchecked")
  public <T> AbstractAssetPool<T> getAssetPool(Class<T> assetClass) {
    AbstractAssetPool<?> assetPool = assetPoolMap.get(assetClass);

    if (assetPool == null) {
      throw new MissingAssetPoolException(assetClass);
    }

    return (AbstractAssetPool<T>) assetPool;
  }
}
