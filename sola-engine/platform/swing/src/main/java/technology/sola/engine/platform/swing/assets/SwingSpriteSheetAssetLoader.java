package technology.sola.engine.platform.swing.assets;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.platform.swing.assets.exception.FailedSpriteSheetLoadException;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

import java.io.IOException;

/**
 * A Swing implementation of the {@link SpriteSheet} {@link AssetLoader}.
 */
public class SwingSpriteSheetAssetLoader extends AssetLoader<SpriteSheet> {
  private final AssetLoader<SolaImage> solaImageAssetLoader;

  /**
   * Creates an instance of this asset loader.
   *
   * @param solaImageAssetLoader the {@link SolaImage} {@link AssetLoader}
   */
  public SwingSpriteSheetAssetLoader(AssetLoader<SolaImage> solaImageAssetLoader) {
    this.solaImageAssetLoader = solaImageAssetLoader;
  }

  @Override
  public Class<SpriteSheet> getAssetClass() {
    return SpriteSheet.class;
  }

  @Override
  protected AssetHandle<SpriteSheet> loadAsset(String path) {
    AssetHandle<SpriteSheet> spriteSheetAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      try {
        String jsonString = PathUtils.readContents(path);
        JsonObject spriteSheetJson = new SolaJson().parse(jsonString).asObject();
        String spriteImageName = spriteSheetJson.getString("spriteSheet");
        String spriteImagePath = PathUtils.getParentPath(path) + "/" + spriteImageName;

        solaImageAssetLoader.getNewAsset(spriteImageName, spriteImagePath)
          .executeWhenLoaded(solaImage -> {
            SpriteSheet spriteSheet = new SpriteSheet(solaImage);

            spriteSheetJson.getArray("sprites").forEach(spritesJsonEntry -> {
              JsonObject spriteJson = spritesJsonEntry.asObject();

              spriteSheet.addSpriteDefinition(
                spriteJson.getString("id"),
                spriteJson.getInt("x"),
                spriteJson.getInt("y"),
                spriteJson.getInt("w"),
                spriteJson.getInt("h")
              );
            });

            spriteSheetAssetHandle.setAsset(spriteSheet);
          });
      } catch (IOException ex) {
        throw new FailedSpriteSheetLoadException(path);
      }
    }).start();

    return spriteSheetAssetHandle;
  }
}