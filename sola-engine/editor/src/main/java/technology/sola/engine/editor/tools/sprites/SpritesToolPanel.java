package technology.sola.engine.editor.tools.sprites;

import javafx.application.Platform;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.engine.editor.tools.ToolPanel;
import technology.sola.json.JsonObject;

/**
 * SpritesToolPanel is a {@link ToolPanel} for managing {@link technology.sola.engine.assets.graphics.SpriteSheet}
 * assets.
 */
public class SpritesToolPanel extends ToolPanel<SpritesToolConfig> {
  private final TabbedPanel tabbedPanel;
  private final SpritesAssetTree spritesAssetTree;


  /**
   * Initializes the tool panel instance.
   *
   * @param editorConfig the {@link EditorConfig} instance
   */
  public SpritesToolPanel(EditorConfig editorConfig) {
    super(editorConfig);

    tabbedPanel = new TabbedPanel();
    spritesAssetTree = new SpritesAssetTree(tabbedPanel);

    getItems().addAll(
      spritesAssetTree,
      tabbedPanel
    );

    Platform.runLater(() -> {
      spritesAssetTree.restoreOpenedFilesAndSelection(toolConfig.openedFileIds(), toolConfig.openId());

      setDividerPositions(toolConfig.dividerPosition());
    });
  }

  @Override
  public String getToolLabel() {
    return "Sprites";
  }

  @Override
  public String getToolId() {
    return "sprites";
  }

  @Override
  public JsonObject buildToolConfigForSaving() {
    var config = new SpritesToolConfig(
      tabbedPanel.getOpenedTabIds(),
      getDividers().get(0).getPosition(),
      tabbedPanel.getSelectedId()
    );

    return new SpritesToolConfig.ConfigJsonMapper().toJson(config);
  }

  @Override
  protected SpritesToolConfig buildToolConfigFromEditorConfig(EditorConfig editorConfig) {
    var toolConfigJson = editorConfig.toolConfigurations().get(getToolId());

    if (toolConfigJson == null) {
      return new SpritesToolConfig();
    }

    return new SpritesToolConfig.ConfigJsonMapper().toObject(toolConfigJson);
  }
}
