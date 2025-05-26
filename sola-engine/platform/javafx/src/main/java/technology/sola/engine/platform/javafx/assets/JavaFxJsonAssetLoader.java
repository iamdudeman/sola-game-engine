package technology.sola.engine.platform.javafx.assets;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.platform.javafx.assets.exception.FailedJsonLoadException;
import technology.sola.json.SolaJson;

import java.io.IOException;

/**
 * A JavaFX implementation of the {@link JsonElementAsset} {@link AssetLoader}.
 */
@NullMarked
public class JavaFxJsonAssetLoader extends AssetLoader<JsonElementAsset> {
  @Override
  public Class<JsonElementAsset> getAssetClass() {
    return JsonElementAsset.class;
  }

  @Override
  protected AssetHandle<JsonElementAsset> loadAsset(String path) {
    AssetHandle<JsonElementAsset> jsonElementAssetAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      try {
        String jsonString = JavaFxPathUtils.readContents(path);

        JsonElementAsset jsonElementAsset = new JsonElementAsset(new SolaJson().parse(jsonString));

        jsonElementAssetAssetHandle.setAsset(jsonElementAsset);
      } catch (IOException ex) {
        throw new FailedJsonLoadException(path);
      }
    }).start();

    return jsonElementAssetAssetHandle;
  }
}
