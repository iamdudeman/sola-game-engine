package technology.sola.engine.editor.tools.assetlist;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.engine.editor.core.utils.ToastService;
import technology.sola.engine.editor.tools.ToolPanel;
import technology.sola.json.JsonObject;

import java.io.IOException;

/**
 * AssetListToolPanel is a {@link ToolPanel} for managing the list of assets loaded via
 * the main {@link technology.sola.engine.assets.list.AssetList}.
 */
@NullMarked
public class AssetListToolPanel extends ToolPanel<AssetListToolConfig> {
  private final AssetListPanel assetListPanel = new AssetListPanel();

  /**
   * Creates an instance of AssetListToolPanel initialized via the {@link EditorConfig}.
   *
   * @param editorConfig the {@link EditorConfig} instance
   */
  public AssetListToolPanel(EditorConfig editorConfig) {
    super(editorConfig);
    var items = getItems();

    items.add(assetListPanel);
  }

  @Override
  public String getToolLabel() {
    return "Asset List";
  }

  @Override
  public String getToolId() {
    return "assetList";
  }

  @Override
  public JsonObject buildToolConfigForSaving() {
    var config = new AssetListToolConfig();

    return new AssetListToolConfig.ConfigJsonMapper().toJson(config);
  }

  @Override
  public void onSwitch() {
    try {
      assetListPanel.update();
    } catch (IOException ex) {
      ToastService.error("Error onSwitch to AssetListToolPanel:" + ex.getMessage());
    }
  }

  @Override
  protected AssetListToolConfig buildToolConfigFromEditorConfig(EditorConfig editorConfig) {
    var toolConfigJson = editorConfig.toolConfigurations().get(getToolId());

    if (toolConfigJson == null) {
      return new AssetListToolConfig();
    }

    return new AssetListToolConfig.ConfigJsonMapper().toObject(toolConfigJson);
  }
}
