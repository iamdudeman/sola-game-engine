package technology.sola.engine.editor.tools.sprites;

import javafx.application.Platform;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.engine.editor.tools.ToolPanel;
import technology.sola.json.JsonObject;

/**
 * SpriteSheetToolPanel is a {@link ToolPanel} for managing {@link SpriteSheet} assets.
 */
public class SpriteSheetToolPanel extends ToolPanel<SpriteSheetToolConfig> {
  private final TabbedPanel tabbedPanel;
  private final SpriteSheetAssetTree spriteSheetAssetTree;


  /**
   * Initializes the tool panel instance.
   *
   * @param editorConfig the {@link EditorConfig} instance
   */
  public SpriteSheetToolPanel(EditorConfig editorConfig) {
    super(editorConfig);

    tabbedPanel = new TabbedPanel();
    spriteSheetAssetTree = new SpriteSheetAssetTree(tabbedPanel);

    getItems().addAll(
      spriteSheetAssetTree,
      tabbedPanel
    );

    Platform.runLater(() -> {
      spriteSheetAssetTree.restoreOpenedFilesAndSelection(toolConfig.openedFileIds(), toolConfig.openId());

      setDividerPositions(toolConfig.dividerPosition());
    });
  }

  @Override
  public String getToolLabel() {
    return "Spritesheet";
  }

  @Override
  public String getToolId() {
    return "spritesheet";
  }

  @Override
  public JsonObject buildToolConfigForSaving() {
    var config = new SpriteSheetToolConfig(
      tabbedPanel.getOpenedTabIds(),
      getDividers().get(0).getPosition(),
      tabbedPanel.getSelectedId()
    );

    return new SpriteSheetToolConfig.ConfigJsonMapper().toJson(config);
  }

  @Override
  protected SpriteSheetToolConfig buildToolConfigFromEditorConfig(EditorConfig editorConfig) {
    var toolConfigJson = editorConfig.toolConfigurations().get(getToolId());

    if (toolConfigJson == null) {
      return new SpriteSheetToolConfig();
    }

    return new SpriteSheetToolConfig.ConfigJsonMapper().toObject(toolConfigJson);
  }
}
