package technology.sola.engine.editor.core.components.assets;

import org.jspecify.annotations.NullMarked;

/**
 * AssetType holds the different asset types that sola editor is capable of managing.
 */
@NullMarked
public enum AssetType {
  AUDIO_CLIP("audio", ".mp3", "Audio", "audio clip"),
  /**
   * Font assets.
   */
  FONT("font", ".font.json", "Fonts", "font"),
  /**
   * Sprites assets.
   */
  SPRITES("sprites", ".sprites.json", "Sprites", "spritesheet"),
  ;

  /**
   * The file extension for the asset type.
   */
  public final String extension;
  final String path;
  final String title;
  final String singleAssetLabel;

  AssetType(String path, String extension, String title, String singleAssetLabel) {
    this.path = path;
    this.extension = extension;
    this.title = title;
    this.singleAssetLabel = singleAssetLabel;
  }
}
