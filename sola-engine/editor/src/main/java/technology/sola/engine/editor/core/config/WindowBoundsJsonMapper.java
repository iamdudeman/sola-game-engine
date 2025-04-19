package technology.sola.engine.editor.core.config;

import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

class WindowBoundsJsonMapper implements JsonMapper<WindowBounds> {
  @Override
  public Class<WindowBounds> getObjectClass() {
    return WindowBounds.class;
  }

  @Override
  public JsonObject toJson(WindowBounds windowBounds) {
    JsonObject json = new JsonObject();

    json.put("x", windowBounds.x());
    json.put("y", windowBounds.y());
    json.put("width", windowBounds.width());
    json.put("height", windowBounds.height());

    return json;
  }

  @Override
  public WindowBounds toObject(JsonObject jsonObject) {
    return new WindowBounds(
      jsonObject.getInt("x"),
      jsonObject.getInt("y"),
      jsonObject.getInt("width"),
      jsonObject.getInt("height")
    );
  }
}
