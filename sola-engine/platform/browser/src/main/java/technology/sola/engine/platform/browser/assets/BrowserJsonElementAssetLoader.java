package technology.sola.engine.platform.browser.assets;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.platform.browser.javascript.JsJsonUtils;
import technology.sola.json.JsonElement;
import technology.sola.json.SolaJson;

/**
 * A browser implementation of the {@link JsonElementAsset} {@link AssetLoader}.
 */
public class BrowserJsonElementAssetLoader extends AssetLoader<JsonElementAsset> {
  @Override
  public Class<JsonElementAsset> getAssetClass() {
    return JsonElementAsset.class;
  }

  @Override
  protected AssetHandle<JsonElementAsset> loadAsset(String path) {
    AssetHandle<JsonElementAsset> jsonElementAssetAssetHandle = new AssetHandle<>();

    JsJsonUtils.loadJson(path, jsonString -> {
      SolaJson solaJson = new SolaJson();
      JsonElement jsonElement = solaJson.parse(jsonString);

      jsonElementAssetAssetHandle.setAsset(new JsonElementAsset(jsonElement));
    });

    return jsonElementAssetAssetHandle;
  }
}
