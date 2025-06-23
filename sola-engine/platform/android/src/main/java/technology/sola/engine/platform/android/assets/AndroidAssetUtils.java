package technology.sola.engine.platform.android.assets;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class AndroidAssetUtils {
  public static String sanitizeAssetPath(String path) {
    return path.replace("assets/", "");
  }

  public static String getFileExtension(String path) {
    return path.substring(path.lastIndexOf('.')).toLowerCase();
  }
}
