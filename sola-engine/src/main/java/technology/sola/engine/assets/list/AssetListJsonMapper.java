package technology.sola.engine.assets.list;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.Asset;
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
    jsonObject.put("spriteSheets", toJsonArray(object.spriteSheetAssets()));
    jsonObject.put("scenes", toJsonArray(object.sceneAssets()));

    return jsonObject;
  }

  @Override
  public AssetList toObject(JsonObject jsonObject) {
    return new AssetList(
      parseAssetDetails(jsonObject.getArray("audioClips", new JsonArray())),
      parseAssetDetails(jsonObject.getArray("fonts", new JsonArray())),
      parseAssetDetails(jsonObject.getArray("guiJsonDocuments", new JsonArray())),
      parseAssetDetails(jsonObject.getArray("spriteSheets", new JsonArray())),
      parseAssetDetails(jsonObject.getArray("scenes", new JsonArray()))
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
