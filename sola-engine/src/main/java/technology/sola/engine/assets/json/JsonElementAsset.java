package technology.sola.engine.assets.json;

import technology.sola.engine.assets.Asset;
import technology.sola.json.JsonElement;

/**
 * Represents a JSON file {@link Asset} that was loaded.
 *
 * @param jsonElement the loaded and parsed JSON file as a {@link JsonElement}
 */
public record JsonElementAsset(JsonElement jsonElement) implements Asset {
}
