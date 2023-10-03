package technology.sola.engine.assets.graphics.gui;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.json.GuiDocumentJsonParser;

/**
 * GuiJsonDocumentAssetLoader is an {@link AssetLoader} for {@link GuiJsonDocument}s.
 */
public class GuiJsonDocumentAssetLoader extends AssetLoader<GuiJsonDocument> {
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;
  private final GuiDocumentJsonParser guiDocumentJsonParser;

  /**
   * Creates an instance of this asset loader.
   *
   * @param jsonElementAssetAssetLoader the {@link AssetLoader} for {@link JsonElementAsset} used internally
   * @param guiDocumentJsonParser       the {@link GuiDocumentJsonParser} used internally
   */
  public GuiJsonDocumentAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader, GuiDocumentJsonParser guiDocumentJsonParser) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
    this.guiDocumentJsonParser = guiDocumentJsonParser;
  }

  @Override
  public Class<GuiJsonDocument> getAssetClass() {
    return GuiJsonDocument.class;
  }

  @Override
  protected AssetHandle<GuiJsonDocument> loadAsset(String path) {
    AssetHandle<GuiJsonDocument> guiJsonDocumentAssetHandle = new AssetHandle<>();

    jsonElementAssetAssetLoader.getNewAsset(path, path)
      .executeWhenLoaded(jsonElementAsset -> {
        GuiElement<?> rootEle = guiDocumentJsonParser.parse(jsonElementAsset.asObject());

        guiJsonDocumentAssetHandle.setAsset(new GuiJsonDocument(rootEle));
      });

    return guiJsonDocumentAssetHandle;
  }
}
