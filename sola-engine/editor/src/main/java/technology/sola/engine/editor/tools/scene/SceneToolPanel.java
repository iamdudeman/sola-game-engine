package technology.sola.engine.editor.tools.scene;

import javafx.application.Platform;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.SolaEditorCustomization;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.engine.editor.tools.ToolPanel;
import technology.sola.json.JsonObject;

/**
 * SceneToolPanel is a {@link ToolPanel} for managing {@link technology.sola.engine.assets.scene.Scene} assets.
 */
@NullMarked
public class SceneToolPanel extends ToolPanel<SceneToolConfig> {
  private final EntityComponentsPanel entityComponentsPanel;
  private final EntityTreeView entityTreeView;
  private final SceneActions sceneActions;

  /**
   * Creates an instance of SceneToolPanel initialized via the {@link EditorConfig}.
   *
   * @param editorConfig the {@link EditorConfig} instance
   */
  public SceneToolPanel(EditorConfig editorConfig, SolaEditorCustomization solaEditorCustomization) {
    super(editorConfig);
    var items = getItems();

    entityComponentsPanel = new EntityComponentsPanel(solaEditorCustomization.componentEditorModules());
    entityComponentsPanel.setMinWidth(150);

    entityTreeView = new EntityTreeView(entityComponentsPanel);
    entityTreeView.setMinWidth(150);

    sceneActions = new SceneActions(toolConfig.lastOpenedScene(), solaEditorCustomization, entityTreeView, entityComponentsPanel);

    items.addAll(
      sceneActions,
      new WorldPreviewPanel(),
      entityComponentsPanel
    );

    Platform.runLater(() -> {
      setDividerPositions(toolConfig.leftDivider(), toolConfig.rightDivider());
    });
  }

  @Override
  public String getToolLabel() {
    return "Scene";
  }

  @Override
  public String getToolId() {
    return "scene";
  }

  @Override
  public JsonObject buildToolConfigForSaving() {
    var config = new SceneToolConfig(
      getDividers().get(0).getPosition(),
      getDividers().get(1).getPosition(),
      sceneActions.getActiveSceneFile()
    );

    return new SceneToolConfig.ConfigJsonMapper().toJson(config);
  }

  @Override
  protected SceneToolConfig buildToolConfigFromEditorConfig(EditorConfig editorConfig) {
    var toolConfigJson = editorConfig.toolConfigurations().get(getToolId());

    if (toolConfigJson == null) {
      return new SceneToolConfig();
    }

    return new SceneToolConfig.ConfigJsonMapper().toObject(toolConfigJson);
  }
}
