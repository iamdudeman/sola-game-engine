package technology.sola.engine.assets;

/**
 * Utilities for helping with asset loading.
 */
public class AssetUtils {
  /**
   * Constant for "wav" audio file extension.
   */
  public static final String EXTENSION_WAV = "wav";
  /**
   * Constant for "mp3" audio file extension.
   */
  public static final String EXTENSION_MP3 = "mp3";
  /**
   * Constant for "json" file extension.
   */
  public static final String EXTENSION_JSON = "json";
  /**
   * Constant for "png" image file extension.
   */
  public static final String EXTENSION_PNG = "png";
  /**
   * Constant for "jpg" image file extension.
   */
  public static final String EXTENSION_JPG = "jpg";

  /**
   * Gets the extension for a path. This will be the extension without the leading period.
   *
   * @param path the path to get the extension for
   * @return the extension
   */
  public static String getExtension(String path) {
    return path.substring(path.lastIndexOf('.') + 1).toLowerCase();
  }

  /**
   * Asserts that a file has a given extension.
   *
   * @param path       the path for the file
   * @param extensions the valid extensions for the file
   */
  public static void assertFileExtension(String path, String... extensions) {
    String extension = getExtension(path);

    for (String ext : extensions) {
      if (extension.equalsIgnoreCase(ext)) {
        return;
      }
    }

    throw new IllegalArgumentException("File extension must be one of: " + String.join(", ", extensions));
  }

  private AssetUtils() {
  }
}
