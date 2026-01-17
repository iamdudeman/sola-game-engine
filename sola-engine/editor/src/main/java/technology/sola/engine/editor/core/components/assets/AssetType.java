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
   * Gui assets.
   */
  GUI("gui", "Gui", "gui", new String[]{".gui.json"}),
  /**
   * Image assets.
   */
  IMAGES("images", "Images", "image", new String[]{".png", ".jpg"}),
  /**
   * Scene assets.
   */
  SCENES("scenes", "Scenes", "scene", new String[]{".scene.json"}),
  /**
   * Sprites assets.
   */
  SPRITES("sprites", "Sprites", "spritesheet", new String[]{".sprites.json"}),
  ;

  public final String path;
  final String title;
  final String singleAssetLabel;
  private final String[] extensions;

  AssetType(String path, String title, String singleAssetLabel, String[] extensions) {
    this.path = path;
    this.extensions = extensions;
    this.title = title;
    this.singleAssetLabel = singleAssetLabel;
  }

  /**
   * Checks if the given filename matches any of the extensions for this asset type.
   *
   * @param name the filename to check
   * @return true if the filename matches an extension for this asset type, false otherwise
   */
  public boolean matchesFilename(String name) {
    for (var extension : extensions) {
      if (name.endsWith(extension)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Removes the extension from the given filename.
   *
   * @param filename the filename to remove the extension from
   * @return the filename without the extension
   */
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

  /**
   * Renames a filename while preserving the extension. The new filename must not contain the extension.
   *
   * @param fileNameWithExtension   the original filename with extension
   * @param newNameWithoutExtension the new filename without extension
   * @return the new filename with extension
   */
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
