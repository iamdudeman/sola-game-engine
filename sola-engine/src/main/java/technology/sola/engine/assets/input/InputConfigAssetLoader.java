package technology.sola.engine.assets.input;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.defaults.input.ControlInput;
import technology.sola.json.JsonArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputConfigAssetLoader extends AssetLoader<InputConfig> {
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;

  public InputConfigAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
  }

  @Override
  public Class<InputConfig> getAssetClass() {
    return InputConfig.class;
  }

  @Override
  protected AssetHandle<InputConfig> loadAsset(String path) {
    AssetHandle<InputConfig> inputConfigAssetHandle = new AssetHandle<>();

    jsonElementAssetAssetLoader.getNewAsset(path, path)
      .executeWhenLoaded(jsonElementAsset -> {
        Map<String, List<List<ControlInput<?>>>> controlMap = new HashMap<>();
        JsonArray inputConfigJson = jsonElementAsset.asArray();

        // todo populate controlMap from inputConfigJson

        inputConfigAssetHandle.setAsset(new InputConfig(controlMap));
      });


    return inputConfigAssetHandle;
  }
}

/*
todo
this could be populated by a controls.input.json file or something like that
[
  {
    "id": "jump",
    "inputs": [
      [{ "type": "key", "key": "A", "state": "PRESSED" }, { "type": "key", "key": "SHIFT", "state": "PRESSED" }]
    ]
  },
  {
    "id": "jump",
    "inputs": [
      { "type": "mouse", "button": "PRIMARY", "state": "PRESSED" }
    ]
  }
]
 */
