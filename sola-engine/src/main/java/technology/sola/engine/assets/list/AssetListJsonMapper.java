package technology.sola.engine.assets.list;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.Asset;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.assets.scene.Scene;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.List;

/**
 * AssetListJsonMapper is a {@link JsonMapper} implementation for {@link AssetList}.
 */
@NullMarked
public class AssetListJsonMapper implements JsonMapper<AssetList> {
  @Override
  public Class<AssetList> getObjectClass() {
    return AssetList.class;
  }

  @Override
  public JsonObject toJson(AssetList object) {
    JsonObject jsonObject = new JsonObject();

    jsonObject.put("audioClips", toJsonArray(object.audioAssets()));
    jsonObject.put("fonts", toJsonArray(object.fontAssets()));
    jsonObject.put("guiJsonDocuments", toJsonArray(object.guiAssets()));
    jsonObject.put("images", toJsonArray(object.imageAssets()));
    jsonObject.put("scenes", toJsonArray(object.sceneAssets()));
    jsonObject.put("spriteSheets", toJsonArray(object.spriteSheetAssets()));

    return jsonObject;
  }

  @Override
  public AssetList toObject(JsonObject jsonObject) {
    return new AssetList(
      parseAssetDetails(jsonObject.<AudioClip>getArray("audioClips", new JsonArray())),
      parseAssetDetails(jsonObject.<Font>getArray("fonts", new JsonArray())),
      parseAssetDetails(jsonObject.<GuiJsonDocument>getArray("guiJsonDocuments", new JsonArray())),
      parseAssetDetails(jsonObject.<SolaImage>getArray("images", new JsonArray())),
      parseAssetDetails(jsonObject.<Scene>getArray("scenes", new JsonArray())),
      parseAssetDetails(jsonObject.<SpriteSheet>getArray("spriteSheets", new JsonArray()))
    );
  }

  private <T extends Asset> JsonArray toJsonArray(List<AssetList.AssetDetails<T>> assetDetails) {
    JsonArray jsonArray = new JsonArray();

    for (var assetDetail : assetDetails) {
      jsonArray.add(new JsonObject()
        .put("id", assetDetail.id())
        .put("path", assetDetail.path())
        .put("isBlocking", assetDetail.isBlocking())
      );
    }


    return jsonArray;
  }

  private <T extends Asset> List<AssetList.AssetDetails<T>> parseAssetDetails(JsonArray jsonArray) {
    return jsonArray.stream()
      .map(jsonElement -> {
        var jsonObject = jsonElement.asObject();

        return new AssetList.AssetDetails<T>(
          jsonObject.getString("id"),
          jsonObject.getString("path"),
          jsonObject.getBoolean("isBlocking")
        );
      })
      .toList();
  }
}
