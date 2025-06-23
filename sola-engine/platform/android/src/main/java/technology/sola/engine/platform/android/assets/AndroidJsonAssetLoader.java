package technology.sola.engine.platform.android.assets;

import android.content.res.AssetManager;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.json.SolaJson;

import java.io.IOException;
import java.util.Scanner;

@NullMarked
public class AndroidJsonAssetLoader extends AssetLoader<JsonElementAsset> {
  private final AssetManager assetManager;

  public AndroidJsonAssetLoader(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  @Override
  public Class<JsonElementAsset> getAssetClass() {
    return JsonElementAsset.class;
  }

  @Override
  protected AssetHandle<JsonElementAsset> loadAsset(String path) {
    AssetHandle<JsonElementAsset> jsonElementAssetAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      try (
        var inputStream = assetManager.open(AndroidAssetUtils.sanitizeAssetPath(path));
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
      ) {
        String jsonString = scanner.next();

        JsonElementAsset jsonElementAsset = new JsonElementAsset(new SolaJson().parse(jsonString));

        jsonElementAssetAssetHandle.setAsset(jsonElementAsset);
      } catch (IOException ex) {
        throw new FailedJsonLoadException(path);
      }
    }).start();

    return jsonElementAssetAssetHandle;
  }
}
