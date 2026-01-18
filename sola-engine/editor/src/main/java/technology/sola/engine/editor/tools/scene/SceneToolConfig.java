package technology.sola.engine.editor.tools.scene;

import org.jspecify.annotations.NullMarked;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

/**
 * SceneToolConfig contains the configuration for the scene tool.
 *
 * @param leftDivider  the position of the left divider in the window
 * @param rightDivider the position of the right divider in the window
 */
@NullMarked
public record SceneToolConfig(
  double leftDivider,
  double rightDivider
) {
  /**
   * Creates an SceneToolConfig with default values (no previously opened files).
   */
  public SceneToolConfig() {
    this(0.2, 0.8);
  }

  static class ConfigJsonMapper implements JsonMapper<SceneToolConfig> {
    @Override
    public Class<SceneToolConfig> getObjectClass() {
      return SceneToolConfig.class;
    }

    @Override
    public JsonObject toJson(SceneToolConfig config) {
      JsonObject json = new JsonObject();

      json.put("leftDivider", config.leftDivider());
      json.put("rightDivider", config.rightDivider());

      return json;
    }

    @Override
    public SceneToolConfig toObject(JsonObject jsonObject) {
      return new SceneToolConfig(
        jsonObject.getDouble("leftDivider", 0.2),
        jsonObject.getDouble("rightDivider", 0.8)
      );
    }
  }
}
