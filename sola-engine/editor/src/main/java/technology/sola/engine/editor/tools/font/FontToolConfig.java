package technology.sola.engine.editor.tools.font;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * FontToolConfig contains the configuration for the font tool.
 *
 * @param openedFileIds   the previously opened file ids
 * @param dividerPosition the position of the divider in the window
 * @param openId          the previously selected file id
 */
@NullMarked
public record FontToolConfig(
  List<String> openedFileIds,
  double dividerPosition,
  @Nullable String openId
) {
  /**
   * Creates a FontToolConfig with default values (no previously opened files).
   */
  public FontToolConfig() {
    this(List.of(), 0.2, null);
  }

  static class ConfigJsonMapper implements JsonMapper<FontToolConfig> {
    @Override
    public JsonObject toJson(FontToolConfig config) {
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
    public FontToolConfig toObject(JsonObject jsonObject) {
      List<String> openedFileIds = new ArrayList<>();

      jsonObject.getArray("openedFiles", new JsonArray())
        .forEach(jsonElement -> openedFileIds.add(jsonElement.asString()));

      return new FontToolConfig(
        openedFileIds,
        jsonObject.getDouble("dividerPosition", 0.2),
        jsonObject.getString("openId", null)
      );
    }
  }
}
