package technology.sola.engine.platform.browser.assets;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.platform.browser.javascript.JsJsonUtils;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

public class BrowserSpriteSheetAssetPool extends AssetPool<SpriteSheet> {
  private final AssetPool<SolaImage> solaImageAssetPool;

  public BrowserSpriteSheetAssetPool(AssetPool<SolaImage> solaImageAssetPool) {
    this.solaImageAssetPool = solaImageAssetPool;
  }

  @Override
  public Class<SpriteSheet> getAssetClass() {
    return SpriteSheet.class;
  }

  @Override
  protected AssetHandle<SpriteSheet> loadAsset(String path) {
    AssetHandle<SpriteSheet> spriteSheetAssetHandle = new AssetHandle<>();

    JsJsonUtils.loadJson(path, jsonString -> {
      SolaJson solaJson = new SolaJson();
      JsonObject spriteSheetJson = solaJson.parse(jsonString).asObject();

      String spriteImageName = spriteSheetJson.getString("spriteSheet");
      solaImageAssetPool.addAndGetAsset(spriteImageName, path.substring(0, path.lastIndexOf("/")) + "/" + spriteImageName).executeWhenLoaded(solaImage -> {
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
