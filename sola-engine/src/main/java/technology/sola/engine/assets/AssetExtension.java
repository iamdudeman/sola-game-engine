package technology.sola.engine.assets;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Enumeration of support {@link Asset} file extensions.
 */
public enum AssetExtension {
  /**
   * The "jpg" {@link technology.sola.engine.assets.graphics.SolaImage} file extension.
   */
  JPG("jpg"),
  /**
   * The "json" {@link technology.sola.engine.assets.json.JsonElementAsset} file extension.
   */
  JSON("json"),
  /**
   * The "mp3" {@link technology.sola.engine.assets.audio.AudioClip} file extension.
   */
  MP3("mp3"),
  /**
   * The "png" {@link technology.sola.engine.assets.graphics.SolaImage} file extension.
   */
  PNG("png"),
  /**
   * The "wav" {@link technology.sola.engine.assets.audio.AudioClip} file extension.
   */
  WAV("wav"),
  ;

  private final String extension;

  AssetExtension(String extension) {
    this.extension = extension;
  }

  /**
   * Gets the {@link AssetExtension} from a file path. Throws {@link IllegalArgumentException} if the extension is not
   * supported.
   *
   * @param path the file path
   * @return the {@link AssetExtension} for the file path
   */
  public static AssetExtension fromPath(String path) {
    String extension = path.substring(path.lastIndexOf('.') + 1).toLowerCase();

    for (AssetExtension ext : values()) {
      if (ext.extension.equalsIgnoreCase(extension)) {
        return ext;
      }
    }

    throw new IllegalArgumentException("Unsupported asset extension: " + extension);
  }

  /**
   * Asserts that a file has a given extension.
   *
   * @param path            the path for the file
   * @param assetExtensions the valid assetExtensions for the file
   */
  public static void assertPathExtension(String path, AssetExtension... assetExtensions) {
    assertExtension(fromPath(path), assetExtensions);
  }

  /**
   * Asserts that a file has a given extension.
   *
   * @param extension            the extension to test
   * @param assetExtensions the valid assetExtensions for the file
   */
  public static void assertExtension(AssetExtension extension, AssetExtension... assetExtensions) {
    for (var assetExtension : assetExtensions) {
      if (extension == assetExtension) {
        return;
      }
    }

    var allowedExtensions = Arrays.stream(assetExtensions)
      .map(assetExtension -> assetExtension.extension)
      .collect(Collectors.joining(", "));

    throw new IllegalArgumentException("File extension must be one of: " + allowedExtensions);
  }
}
