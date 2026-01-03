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
  AUDIO_CLIP("audio", "Audio", "audio clip", ".mp3"),
  /**
   * Font assets.
   */
  FONT("font", "Fonts", "font", ".font.json"),
  /**
   * Sprites assets.
   */
  SPRITES("sprites", "Sprites", "spritesheet", ".sprites.json"),
  ;

  /**
   * The file extension for the asset type.
   */
  public final String extension;
  final String path;
  final String title;
  final String singleAssetLabel;

  AssetType(String path, String title, String singleAssetLabel, String extension) {
    this.path = path;
    this.extension = extension;
    this.title = title;
    this.singleAssetLabel = singleAssetLabel;
  }

  public boolean matchesFilename(String name) {
    return name.endsWith(extension);
  }

  public String removeExtension(String filename) {
    return filename.replace(extension, "");
  }

  public String editFilename(String fileNameWithExtension, String newNameWithoutExtension) {
    return newNameWithoutExtension + extension;
  }
}
