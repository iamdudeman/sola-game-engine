package technology.sola.engine.editor.tools.scene;

import javafx.application.Platform;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.engine.editor.tools.ToolPanel;
import technology.sola.json.JsonObject;

/**
 * SceneToolPanel is a {@link ToolPanel} for managing {@link technology.sola.engine.assets.scene.Scene} assets.
 */
@NullMarked
public class SceneToolPanel extends ToolPanel<SceneToolConfig> {
  /**
   * Creates an instance of SceneToolPanel initialized via the {@link EditorConfig}.
   *
   * @param editorConfig the {@link EditorConfig} instance
   */
  public SceneToolPanel(EditorConfig editorConfig) {
    super(editorConfig);
    var items = getItems();

    // todo need to figure out how to create a new scene
    // todo should open last opened scene
    // todo how to switch to another scene?

    items.addAll(
      new EditorPanel(), // todo entity panel
      new EditorPanel(), // todo World rendering (should have a Play button or something like that)
      new EditorPanel()  // todo entity info panel
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
      getDividers().get(1).getPosition()
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
