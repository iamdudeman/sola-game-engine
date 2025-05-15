package technology.sola.engine.editor.tools.sprites;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.engine.editor.tools.ToolPanel;
import technology.sola.json.JsonObject;

/**
 * SpriteSheetToolPanel is a {@link ToolPanel} for managing {@link SpriteSheet} assets.
 */
public class SpriteSheetToolPanel extends ToolPanel<SpriteSheetToolConfig> {
  private final SpriteSheetState spriteSheetState = new SpriteSheetState();
  private TabbedPanel tabbedPanel;
  private SpriteSheetAssetTree spriteSheetAssetTree;
  private SplitPane topPane;

  /**
   * Initializes the tool panel instance.
   *
   * @param editorConfig the {@link EditorConfig} instance
   */
  public SpriteSheetToolPanel(EditorConfig editorConfig) {
    super(editorConfig);

    orientationProperty().set(Orientation.VERTICAL);

    var selectedSpriteInfoPanel = new SelectedSpriteInfoPanel(spriteSheetState);

    buildTopPane(selectedSpriteInfoPanel);

    getItems().addAll(topPane, selectedSpriteInfoPanel);

    Platform.runLater(() -> {
      spriteSheetAssetTree.restoreOpenedFilesAndSelection(toolConfig.openedFileIds(), toolConfig.openId());

      setDividerPositions(toolConfig.topBottomDivider());
      topPane.setDividerPositions(toolConfig.leftDivider(), toolConfig.rightDivider());
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
      topPane.getDividers().get(0).getPosition(),
      topPane.getDividers().get(1).getPosition(),
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

  private void buildTopPane(SelectedSpriteInfoPanel selectedSpriteInfoPanel) {
    topPane = new SplitPane();

    tabbedPanel = new TabbedPanel();
    var spritesTreeView = new SpritesTreeView(spriteSheetState, selectedSpriteInfoPanel);
    spriteSheetAssetTree = new SpriteSheetAssetTree(spriteSheetState, tabbedPanel, spritesTreeView);

    spriteSheetAssetTree.setMinWidth(200);
    spritesTreeView.setMinWidth(200);

    topPane.getItems().addAll(
      spriteSheetAssetTree,
      tabbedPanel,
      spritesTreeView
    );
  }
}
