package technology.sola.engine.assets;

import java.io.File;

public interface AssetMapper<T> {
  Class<T> getAssetType();

  T mapToAssetType(File file);
}
