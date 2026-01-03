package technology.sola.engine.editor.core.components.assets;

import org.jspecify.annotations.NullMarked;

/**
 * AssetType holds the different asset types that sola editor is capable of managing.
 */
@NullMarked
public enum AssetType {
  /**
   * AudioClip assets.
   */
  AUDIO_CLIP("audio", "Audio", "audio clip", new String[]{".mp3", ".wav"}),
  /**
   * Font assets.
   */
  FONT("font", "Fonts", "font", new String[]{".font.json"}),
  /**
   * Sprites assets.
   */
  SPRITES("sprites", "Sprites", "spritesheet", new String[]{".sprites.json"}),
  ;

  final String path;
  final String title;
  final String singleAssetLabel;
  private final String[] extensions;

  AssetType(String path, String title, String singleAssetLabel, String[] extensions) {
    this.path = path;
    this.extensions = extensions;
    this.title = title;
    this.singleAssetLabel = singleAssetLabel;
  }

  public boolean matchesFilename(String name) {
    for (var extension : extensions) {
      if (name.endsWith(extension)) {
        return true;
      }
    }

    return false;
  }

  public String removeExtension(String filename) {
    String result = filename;

    for (var extension : extensions) {
      if (filename.endsWith(extension)) {
        result = filename.replace(extension, "");
        break;
      }
    }

    return result;
  }

  public String editFilename(String fileNameWithExtension, String newNameWithoutExtension) {
    String extensionMatch = "";

    for (var extension : extensions) {
      if (fileNameWithExtension.endsWith(extension)) {
        extensionMatch = extension;
        break;
      }
    }

    return newNameWithoutExtension + extensionMatch;
  }
}
