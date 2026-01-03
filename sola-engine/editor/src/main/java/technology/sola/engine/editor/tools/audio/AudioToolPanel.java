package technology.sola.engine.editor.tools.audio;

import javafx.application.Platform;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.engine.editor.tools.ToolPanel;
import technology.sola.json.JsonObject;

/**
 * AudioToolPanel is a {@link ToolPanel} for managing {@link technology.sola.engine.assets.audio.AudioClip} assets.
 */
@NullMarked
public class AudioToolPanel extends ToolPanel<AudioToolConfig> {
  private final TabbedPanel tabbedPanel;
  private final AudioAssetTree fontAssetTree;

  /**
   * Creates an instance of AudioToolPanel initialized via the {@link EditorConfig}.
   *
   * @param editorConfig the {@link EditorConfig} instance
   */
  public AudioToolPanel(EditorConfig editorConfig) {
    super(editorConfig);
    var items = getItems();

    tabbedPanel = new TabbedPanel();
    fontAssetTree = new AudioAssetTree(tabbedPanel);

    fontAssetTree.setMinWidth(200);

    items.addAll(fontAssetTree, tabbedPanel);

    Platform.runLater(() -> {
      fontAssetTree.restoreOpenedFilesAndSelection(toolConfig.openedFileIds(), toolConfig.openId());

      setDividerPositions(toolConfig.dividerPosition());
    });
  }

  @Override
  public String getToolLabel() {
    return "Audio";
  }

  @Override
  public String getToolId() {
    return "audio";
  }

  @Override
  public JsonObject buildToolConfigForSaving() {
    var config = new AudioToolConfig(
      tabbedPanel.getOpenedTabIds(),
      getDividers().get(0).getPosition(),
      tabbedPanel.getSelectedId()
    );

    return new AudioToolConfig.ConfigJsonMapper().toJson(config);
  }

  @Override
  protected AudioToolConfig buildToolConfigFromEditorConfig(EditorConfig editorConfig) {
    var toolConfigJson = editorConfig.toolConfigurations().get(getToolId());

    if (toolConfigJson == null) {
      return new AudioToolConfig();
    }

    return new AudioToolConfig.ConfigJsonMapper().toObject(toolConfigJson);
  }
}
