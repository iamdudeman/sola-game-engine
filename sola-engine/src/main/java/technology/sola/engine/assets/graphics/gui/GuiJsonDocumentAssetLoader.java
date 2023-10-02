package technology.sola.engine.assets.graphics.gui;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;

// todo add asset loader to platform (possibly via SolaWithDefaults!)
public class GuiJsonDocumentAssetLoader extends AssetLoader<GuiJsonDocument> {
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;

  public GuiJsonDocumentAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
  }

  @Override
  public Class<GuiJsonDocument> getAssetClass() {
    return GuiJsonDocument.class;
  }

  @Override
  protected AssetHandle<GuiJsonDocument> loadAsset(String path) {
    // todo implement this
    return null;
  }
}
