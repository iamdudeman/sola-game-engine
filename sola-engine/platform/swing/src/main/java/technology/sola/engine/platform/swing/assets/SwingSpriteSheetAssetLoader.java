package technology.sola.engine.platform.swing.assets;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.platform.swing.assets.exception.FailedSpriteSheetLoadException;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SwingSpriteSheetAssetLoader extends AssetLoader<SpriteSheet> {
  private final AssetLoader<SolaImage> solaImageAssetLoader;

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
      File file = new File(path);

      try {
        String jsonString = Files.readString(file.toPath());
        SolaJson solaJson = new SolaJson();
        JsonObject spriteSheetJson = solaJson.parse(jsonString).asObject();
        String spriteImageName = spriteSheetJson.getString("spriteSheet");
        String spriteImagePath = new File(file.getParent(), spriteImageName).getPath();

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
