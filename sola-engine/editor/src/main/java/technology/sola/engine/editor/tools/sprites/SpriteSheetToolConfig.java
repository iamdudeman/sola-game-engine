package technology.sola.engine.editor.tools.sprites;

import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * SpriteSheetToolConfig contains the configuration for the spritesheet tool.
 *
 * @param openedFileIds    the previously opened file ids
 * @param leftDivider      the position of the left divider in the top pane
 * @param rightDivider     the position of the right divider in the top pane
 * @param topBottomDivider the position of the top and bottom divider in the window
 * @param openId           the previously selected file id
 */
public record SpriteSheetToolConfig(
  List<String> openedFileIds,
  double leftDivider,
  double rightDivider,
  double topBottomDivider,
  String openId
) {
  /**
   * Creates a SpriteSheetToolConfig with default values (no previously opened files).
   */
  public SpriteSheetToolConfig() {
    this(List.of(), 0.2, 0.8, 0.8, null);
  }

  static class ConfigJsonMapper implements JsonMapper<SpriteSheetToolConfig> {
    @Override
    public Class<SpriteSheetToolConfig> getObjectClass() {
      return SpriteSheetToolConfig.class;
    }

    @Override
    public JsonObject toJson(SpriteSheetToolConfig config) {
      JsonObject json = new JsonObject();
      JsonArray openedFiles = new JsonArray();

      config.openedFileIds.forEach(openedFiles::add);

      json.put("openedFiles", openedFiles);
      json.put("leftDivider", config.leftDivider());
      json.put("rightDivider", config.rightDivider());
      json.put("topBottomDivider", config.topBottomDivider());

      if (config.openId == null) {
        json.putNull("openId");
      } else {
        json.put("openId", config.openId());
      }

      return json;
    }

    @Override
    public SpriteSheetToolConfig toObject(JsonObject jsonObject) {
      List<String> openedFileIds = new ArrayList<>();

      jsonObject.getArray("openedFiles", new JsonArray())
        .forEach(jsonElement -> openedFileIds.add(jsonElement.asString()));

      return new SpriteSheetToolConfig(
        openedFileIds,
        jsonObject.getDouble("leftDivider", 0.2),
        jsonObject.getDouble("rightDivider", 0.8),
        jsonObject.getDouble("topBottomDivider", 0.9),
        jsonObject.getString("openId", null)
      );
    }
  }
}
