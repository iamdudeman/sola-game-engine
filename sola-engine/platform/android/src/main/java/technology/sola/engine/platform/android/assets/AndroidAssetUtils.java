package technology.sola.engine.platform.android.assets;

import org.jspecify.annotations.NullMarked;

/**
 * Utility methods for Android assets.
 */
@NullMarked
public class AndroidAssetUtils {
  /**
   * Sanitizes a sola asset path for its location in Android.
   *
   * @param path the sola asset path
   * @return the sola asset path on Android
   */
  public static String sanitizeAssetPath(String path) {
    return path.replace("assets/", "");
  }

  /**
   * Gets the file extension for an asset path.
   *
   * @param path the sola asset path
   * @return the file extension including "."
   */
  public static String getFileExtension(String path) {
    return path.substring(path.lastIndexOf('.')).toLowerCase();
  }
}
