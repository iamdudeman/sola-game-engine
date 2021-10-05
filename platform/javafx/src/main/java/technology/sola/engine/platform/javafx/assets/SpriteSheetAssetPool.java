package technology.sola.engine.platform.javafx.assets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.sprite.SpriteSheet;
import technology.sola.engine.platform.javafx.assets.exception.FailedSpriteSheetLoadException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    Gson gson = new Gson();
    File file = new File(path);

    try (JsonReader reader = new JsonReader(new FileReader(file))) {
      JsonObject spriteSheetJson = gson.fromJson(reader, JsonObject.class);

      String spriteImageName = spriteSheetJson.get("spriteSheet").getAsString();
      SolaImage spriteImage = solaImageAssetPool.addAndGetAsset(spriteImageName, new File(file.getParent(), spriteImageName).getPath());
      SpriteSheet spriteSheet = new SpriteSheet(spriteImage);

      spriteSheetJson.getAsJsonArray("sprites").forEach(spritesJsonEntry -> {
        JsonObject spriteJson = spritesJsonEntry.getAsJsonObject();

        spriteSheet.addSpriteDefinition(
          spriteJson.get("id").getAsString(),
          spriteJson.get("x").getAsInt(),
          spriteJson.get("y").getAsInt(),
          spriteJson.get("w").getAsInt(),
          spriteJson.get("h").getAsInt()
        );
      });

      return spriteSheet;
    } catch (IOException ex) {
      throw new FailedSpriteSheetLoadException(path);
    }
  }
}
