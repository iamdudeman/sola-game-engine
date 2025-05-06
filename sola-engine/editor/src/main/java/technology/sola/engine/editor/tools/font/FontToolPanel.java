package technology.sola.engine.editor.tools.font;

import javafx.application.Platform;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.tools.ToolPanel;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.json.JsonObject;

/**
 * FontToolConfig is a {@link ToolPanel} for managing {@link technology.sola.engine.assets.graphics.font.Font} assets.
 */
public class FontToolPanel extends ToolPanel<FontToolConfig> {
  private final TabbedPanel tabbedPanel;
  private final FontAssetTree fontAssetTree;

  /**
   * Creates an instance of FontToolPanel initialized via the {@link EditorConfig}.
   *
   * @param editorConfig the {@link EditorConfig} instance
   */
  public FontToolPanel(EditorConfig editorConfig) {
    super(editorConfig);
    var items = getItems();

    tabbedPanel = new TabbedPanel();
    fontAssetTree = new FontAssetTree(tabbedPanel);

    items.addAll(fontAssetTree, tabbedPanel);

    Platform.runLater(() -> {
      fontAssetTree.restoreOpenedFilesAndSelection(toolConfig.openedFileIds(), toolConfig.openId());

      setDividerPositions(toolConfig.dividerPosition());
    });
  }

  @Override
  public String getToolLabel() {
    return "Font";
  }

  @Override
  public String getToolId() {
    return "font";
  }

  @Override
  public JsonObject buildToolConfigForSaving() {
    var config = new FontToolConfig(
      tabbedPanel.getOpenedTabIds(),
      getDividers().get(0).getPosition(),
      tabbedPanel.getSelectedId()
    );

    return new FontToolConfig.ConfigJsonMapper().toJson(config);
  }

  @Override
  protected FontToolConfig buildToolConfigFromEditorConfig(EditorConfig editorConfig) {
    var toolConfigJson = editorConfig.toolConfigurations().get(getToolId());

    if (toolConfigJson == null) {
      return new FontToolConfig();
    }

    return new FontToolConfig.ConfigJsonMapper().toObject(toolConfigJson);
  }
}
