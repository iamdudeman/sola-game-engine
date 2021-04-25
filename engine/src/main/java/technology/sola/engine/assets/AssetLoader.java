package technology.sola.engine.assets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetLoader {
  private static final Logger logger = LoggerFactory.getLogger(AssetLoader.class);
  private final List<AssetMapper<?>> assetMapperList = new ArrayList<>();
  private final Map<String, String> assetPaths = new HashMap<>();

  public void addAssetMapper(AssetMapper<?> assetMapper) {
    assetMapperList.add(assetMapper);
  }

  public void addAsset(String assetId, String resourcePath) {
    assetPaths.put(assetId, resourcePath);
  }

  public <T> T getAsset(String assetId, Class<T> assetClass) {
    String path = assetPaths.get(assetId);
    File file = getResourceFile(path);

    return assetClass.cast(assetMapperList.stream()
      .filter(assetMapper -> assetMapper.getAssetType().equals(assetClass))
      .findFirst()
      .map(assetMapper -> assetMapper.mapToAssetType(file))
      .orElse(null));
  }

  private File getResourceFile(String resourcePath) {
    try {
      URL resource = getClass().getClassLoader().getResource(resourcePath);

      if (resource == null) {
        logger.info("{} not found", resourcePath);
        return null;
      }

      return Paths.get(resource.toURI()).toFile();
    } catch (URISyntaxException ex) {
      logger.error(ex.getMessage(), ex);
      return null;
    }
  }
}
