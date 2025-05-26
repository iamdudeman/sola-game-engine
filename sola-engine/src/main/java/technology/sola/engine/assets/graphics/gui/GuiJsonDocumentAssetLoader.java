package technology.sola.engine.assets.graphics.gui;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.graphics.gui.json.GuiJsonDocumentBuilder;

/**
 * GuiJsonDocumentAssetLoader is an {@link AssetLoader} for {@link GuiJsonDocument}s.
 */
@NullMarked
public class GuiJsonDocumentAssetLoader extends AssetLoader<GuiJsonDocument> {
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;
  private final GuiJsonDocumentBuilder guiJsonDocumentBuilder;

  /**
   * Creates an instance of this asset loader.
   *
   * @param jsonElementAssetAssetLoader the {@link AssetLoader} for {@link JsonElementAsset} used internally
   * @param guiJsonDocumentBuilder      the {@link GuiJsonDocumentBuilder} used internally
   */
  public GuiJsonDocumentAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader, GuiJsonDocumentBuilder guiJsonDocumentBuilder) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
    this.guiJsonDocumentBuilder = guiJsonDocumentBuilder;
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
        GuiJsonDocument guiJsonDocument = guiJsonDocumentBuilder.build(jsonElementAsset.asObject());

        guiJsonDocumentAssetHandle.setAsset(guiJsonDocument);
      });

    return guiJsonDocumentAssetHandle;
  }
}
