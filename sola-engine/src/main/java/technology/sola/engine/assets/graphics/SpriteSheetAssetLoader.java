package technology.sola.engine.assets.graphics;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.json.JsonObject;

public class SpriteSheetAssetLoader extends AssetLoader<SpriteSheet> {
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;
  private final AssetLoader<SolaImage> solaImageAssetLoader;

  public SpriteSheetAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader, AssetLoader<SolaImage> solaImageAssetLoader) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
    this.solaImageAssetLoader = solaImageAssetLoader;
  }

  @Override
  public Class<SpriteSheet> getAssetClass() {
    return SpriteSheet.class;
  }

  @Override
  protected AssetHandle<SpriteSheet> loadAsset(String path) {
    AssetHandle<SpriteSheet> spriteSheetAssetHandle = new AssetHandle<>();

    jsonElementAssetAssetLoader.getNewAsset(path, path)
      .executeWhenLoaded(jsonElementAsset -> {
        JsonObject spriteSheetJson = jsonElementAsset.jsonElement().asObject();

        String spriteImageName = spriteSheetJson.getString("spriteSheet");
        solaImageAssetLoader.getNewAsset(spriteImageName, path.substring(0, path.lastIndexOf("/")) + "/" + spriteImageName).executeWhenLoaded(solaImage -> {
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
      });

    return spriteSheetAssetHandle;
  }
}
