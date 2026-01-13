package technology.sola.engine.assets;

import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.assets.scene.Scene;

import java.util.List;

public record AssetList(
  List<AssetDetails<AudioClip>> audioAssets,
  List<AssetDetails<Font>> fontAssets,
  List<AssetDetails<GuiJsonDocument>> guiAssets,
  List<AssetDetails<SpriteSheet>> spriteSheetAssets,
  List<AssetDetails<Scene>> sceneAssets
) implements Asset {
  public static final String ID = "assets";
  public static final String PATH = "assets/config.assets.json";

  public record AssetDetails<T extends Asset>(
    String id,
    String path,
    boolean isBlocking
  ) {
  }

  // todo method to apply to AssetLoaderProvider
  public static void loadAssetList(AssetLoaderProvider assetLoaderProvider, Runnable onComplete) {
    assetLoaderProvider.get(AssetList.class).getNewAsset(ID, PATH)
      .executeWhenLoaded(assetList -> {



        onComplete.run();
      });
  }
}
