package technology.sola.engine.editor.core.config;

import org.jspecify.annotations.NullMarked;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.HashMap;
import java.util.Map;

@NullMarked
class EditorConfigJsonMapper implements JsonMapper<EditorConfig> {
  private final WindowBoundsJsonMapper windowBoundsJsonMapper = new WindowBoundsJsonMapper();

  @Override
  public Class<EditorConfig> getObjectClass() {
    return EditorConfig.class;
  }

  @Override
  public JsonObject toJson(EditorConfig editorConfig) {
    JsonObject object = new JsonObject();
    JsonObject tools = new JsonObject();

    editorConfig.toolConfigurations().forEach(tools::put);

    object.put("window", windowBoundsJsonMapper.toJson(editorConfig.window()));
    object.put("selectedTool", editorConfig.selectedTool());
    object.put("tools", tools);

    return object;
  }

  @Override
  public EditorConfig toObject(JsonObject jsonObject) {
    Map<String, JsonObject> toolConfigs = new HashMap<>();
    JsonObject tools = jsonObject.getObjectOrNull("tools");

    if (tools != null) {
      tools.forEach((key, value) -> toolConfigs.put(key, value.asObject()));
    }

    return new EditorConfig(
      windowBoundsJsonMapper.toObject(jsonObject.getObject("window")),
      jsonObject.getStringOrNull("selectedTool"),
      toolConfigs
    );
  }
}
