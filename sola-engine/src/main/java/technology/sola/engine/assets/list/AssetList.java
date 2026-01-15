package technology.sola.engine.assets.list;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.Asset;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.assets.scene.Scene;

import java.util.List;

/**
 * AssetList is an {@link Asset} that contains a list of other assets to be loaded.
 *
 * @param audioAssets       the list of {@link AudioClip} assets
 * @param fontAssets        the list of {@link Font} assets
 * @param guiAssets         the list of {@link GuiJsonDocument} assets
 * @param spriteSheetAssets the list of {@link SpriteSheet} assets
 * @param sceneAssets       the list of {@link Scene} assets
 */
@NullMarked
public record AssetList(
  List<AssetDetails<AudioClip>> audioAssets,
  List<AssetDetails<Font>> fontAssets,
  List<AssetDetails<GuiJsonDocument>> guiAssets,
  List<AssetDetails<SpriteSheet>> spriteSheetAssets,
  List<AssetDetails<Scene>> sceneAssets
) implements Asset {
  /**
   * The id of the main asset list.
   */
  public static final String ID = "assets";
  /**
   * The path to the main asset list.
   */
  public static final String PATH = "assets/config.assets.json";

  /**
   * AssetDetails contains details for loading assets on startup of the {@link technology.sola.engine.core.Sola}.
   *
   * @param id         the id of the asset
   * @param path       the path to the asset
   * @param isBlocking whether it should block the {@link technology.sola.engine.core.Sola} from starting while loading or not
   * @param <T>        the type of {@link Asset}
   */
  public record AssetDetails<T extends Asset>(
    String id,
    String path,
    boolean isBlocking
  ) {
  }
}
