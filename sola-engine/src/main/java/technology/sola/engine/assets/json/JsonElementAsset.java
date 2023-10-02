package technology.sola.engine.assets.json;

import technology.sola.engine.assets.Asset;
import technology.sola.json.JsonElement;

public record JsonElementAsset(JsonElement jsonElement) implements Asset {
}
