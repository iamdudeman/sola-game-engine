package technology.sola.engine.editor.tools.assetlist;

import org.jspecify.annotations.NullMarked;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

/**
 * AssetListToolConfig contains the configuration for the asset list tool.
 */
@NullMarked
public record AssetListToolConfig() {
  static class ConfigJsonMapper implements JsonMapper<AssetListToolConfig> {
    @Override
    public Class<AssetListToolConfig> getObjectClass() {
      return AssetListToolConfig.class;
    }

    @Override
    public JsonObject toJson(AssetListToolConfig config) {
      return new JsonObject();
    }

    @Override
    public AssetListToolConfig toObject(JsonObject jsonObject) {
      return new AssetListToolConfig();
    }
  }
}
