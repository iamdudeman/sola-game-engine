package technology.sola.engine.assets.json;

import technology.sola.engine.assets.Asset;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;

/**
 * Represents a JSON file {@link Asset} that was loaded.
 *
 * @param jsonElement the loaded and parsed JSON file as a {@link JsonElement}
 */
public record JsonElementAsset(JsonElement jsonElement) implements Asset {
  /**
   * @return the {@link JsonElement#asObject()}
   */
  public JsonObject asObject() {
    return jsonElement.asObject();
  }

  /**
   * @return the {@link JsonElement#asArray()}
   */
  public JsonArray asArray() {
    return jsonElement.asArray();
  }
}
