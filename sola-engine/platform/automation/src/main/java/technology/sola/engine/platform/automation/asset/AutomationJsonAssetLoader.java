package technology.sola.engine.platform.automation.asset;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.platform.automation.AutomationPathUtils;
import technology.sola.json.SolaJson;

import java.io.IOException;

/**
 * An Automation implementation of the {@link JsonElementAsset} {@link AssetLoader}.
 */
@NullMarked
public class AutomationJsonAssetLoader extends AssetLoader<JsonElementAsset> {
  @Override
  public Class<JsonElementAsset> getAssetClass() {
    return JsonElementAsset.class;
  }

  @Override
  protected AssetHandle<JsonElementAsset> loadAsset(String path) {
    AssetHandle<JsonElementAsset> jsonElementAssetAssetHandle = new AssetHandle<>();

    try {
      String jsonString = AutomationPathUtils.readContents(path);

      JsonElementAsset jsonElementAsset = new JsonElementAsset(new SolaJson().parse(jsonString));

      jsonElementAssetAssetHandle.setAsset(jsonElementAsset);
    } catch (IOException ex) {
      throw new FailedJsonLoadException(path);
    }

    return jsonElementAssetAssetHandle;
  }
}
