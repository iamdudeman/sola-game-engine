package technology.sola.engine.editor.tools.sprites;

import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.List;

record SpritesToolConfig(
  List<String> openedFileIds,
  double dividerPosition,
  String openId
) {
  public SpritesToolConfig() {
    this(List.of(), 0.2, null);
  }

  static class ConfigJsonMapper implements JsonMapper<SpritesToolConfig> {
    @Override
    public Class<SpritesToolConfig> getObjectClass() {
      return SpritesToolConfig.class;
    }

    @Override
    public JsonObject toJson(SpritesToolConfig config) {
      JsonObject json = new JsonObject();
      JsonArray openedFiles = new JsonArray();

      config.openedFileIds.forEach(openedFiles::add);

      json.put("openedFiles", openedFiles);
      json.put("dividerPosition", config.dividerPosition());
      if (config.openId == null) {
        json.putNull("openId");
      } else {
        json.put("openId", config.openId());
      }

      return json;
    }

    @Override
    public SpritesToolConfig toObject(JsonObject jsonObject) {
      List<String> openedFileIds = new ArrayList<>();

      jsonObject.getArray("openedFiles").forEach(jsonElement -> {
        openedFileIds.add(jsonElement.asString());
      });

      return new SpritesToolConfig(
        openedFileIds,
        jsonObject.getDouble("dividerPosition"),
        jsonObject.getString("openId", null)
      );
    }
  }
}
