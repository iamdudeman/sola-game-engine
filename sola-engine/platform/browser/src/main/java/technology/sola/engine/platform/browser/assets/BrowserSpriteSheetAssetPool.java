package technology.sola.engine.platform.browser.assets;

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
  protected SpriteSheet loadAsset(String path) {
    JsJsonUtils.loadJson(path, jsonString -> {
      SolaJson solaJson = new SolaJson();
      JsonObject spriteSheetJson = solaJson.parse(jsonString).asObject();

      String spriteImageName = spriteSheetJson.getString("spriteSheet");
      SolaImage spriteImage = solaImageAssetPool.addAndGetAsset(spriteImageName, path + "/" + spriteImageName);
      SpriteSheet spriteSheet = new SpriteSheet(spriteImage);

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
    });



    return null;
  }
}
