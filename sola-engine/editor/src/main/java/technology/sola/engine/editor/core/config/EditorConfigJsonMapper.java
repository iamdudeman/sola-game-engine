package technology.sola.engine.editor.core.config;

import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

public class EditorConfigJsonMapper implements JsonMapper<EditorConfig> {
  private final WindowBoundsJsonMapper windowBoundsJsonMapper = new WindowBoundsJsonMapper();

  @Override
  public Class<EditorConfig> getObjectClass() {
    return EditorConfig.class;
  }

  @Override
  public JsonObject toJson(EditorConfig editorConfig) {
    JsonObject object = new JsonObject();

    object.put("window", windowBoundsJsonMapper.toJson(editorConfig.window()));
    object.put("selectedTool", editorConfig.selectedTool());

    return object;
  }

  @Override
  public EditorConfig toObject(JsonObject jsonObject) {
    return new EditorConfig(
      windowBoundsJsonMapper.toObject(jsonObject.getObject("window")),
      jsonObject.getString("selectedTool", null)
    );
  }
}
