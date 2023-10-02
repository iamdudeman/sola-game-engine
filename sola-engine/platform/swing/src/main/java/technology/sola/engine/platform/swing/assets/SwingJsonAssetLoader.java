package technology.sola.engine.platform.swing.assets;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.platform.swing.assets.exception.FailedJsonLoadException;
import technology.sola.json.SolaJson;

import java.io.IOException;

/**
 * A Swing implementation of the {@link JsonElementAsset} {@link AssetLoader}.
 */
public class SwingJsonAssetLoader extends AssetLoader<JsonElementAsset> {
  @Override
  public Class<JsonElementAsset> getAssetClass() {
    return JsonElementAsset.class;
  }

  @Override
  protected AssetHandle<JsonElementAsset> loadAsset(String path) {
    AssetHandle<JsonElementAsset> jsonElementAssetAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      try {
        String jsonString = SwingPathUtils.readContents(path);

        JsonElementAsset jsonElementAsset = new JsonElementAsset(new SolaJson().parse(jsonString));

        jsonElementAssetAssetHandle.setAsset(jsonElementAsset);
      } catch (IOException ex) {
        throw new FailedJsonLoadException(path);
      }
    }).start();

    return jsonElementAssetAssetHandle;
  }
}
