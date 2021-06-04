package technology.sola.engine.assets;

import java.util.HashMap;
import java.util.Map;

public class AssetPoolProvider {
  private final Map<Class<?>, AssetPool<?>> assetPoolMap = new HashMap<>();

  public void addAssetPool(AssetPool<?> assetPool) {
    assetPoolMap.put(assetPool.getAssetClass(), assetPool);
  }

  public <T> AssetPool<T> getAssetPool(Class<T> assetClass) {
    // TODO specific exception
    return (AssetPool<T>) assetPoolMap.computeIfAbsent(
      assetClass,
      key -> { throw new RuntimeException("AssetPool for class [" + assetClass + "] does not exist"); }
    );
  }
}
