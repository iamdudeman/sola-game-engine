package technology.sola.engine.platform.swing.assets;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.sprite.SpriteSheet;
import technology.sola.engine.platform.swing.assets.exception.FailedSpriteSheetLoadException;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SpriteSheetAssetPool extends AssetPool<SpriteSheet> {
  private final AssetPool<SolaImage> solaImageAssetPool;

  public SpriteSheetAssetPool(AssetPool<SolaImage> solaImageAssetPool) {
    this.solaImageAssetPool = solaImageAssetPool;
  }

  @Override
  public Class<SpriteSheet> getAssetClass() {
    return SpriteSheet.class;
  }

  @Override
  protected SpriteSheet loadAsset(String path) {
    File file = new File(path);
    try {
      String jsonString = Files.readString(file.toPath());
      SolaJson solaJson = new SolaJson();
      JsonObject spriteSheetJson = solaJson.parse(jsonString).asObject();

      String spriteImageName = spriteSheetJson.getString("spriteSheet");
      SolaImage spriteImage = solaImageAssetPool.addAndGetAsset(spriteImageName, new File(file.getParent(), spriteImageName).getPath());
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

      return spriteSheet;
    } catch (IOException ex) {
      throw new FailedSpriteSheetLoadException(path);
    }
  }
}
