package technology.sola.engine.editor.tools;

import javafx.scene.control.SplitPane;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.json.JsonObject;

/**
 * ToolPanel is a top-level container that tools will extend to provide a common layout and configuration functionality.
 *
 * @param <T> the tool's configuration object type
 */
@NullMarked
public abstract class ToolPanel<T> extends SplitPane {
  /**
   * The configuration for this tool.
   */
  protected T toolConfig;

  /**
   * Initializes the tool panel instance.
   *
   * @param editorConfig the {@link EditorConfig} instance
   */
  public ToolPanel(EditorConfig editorConfig) {
    toolConfig = buildToolConfigFromEditorConfig(editorConfig);
    setId(getToolId());
  }

  /**
   * @return the label in the toolbar for this tool
   */
  public abstract String getToolLabel();

  /**
   * @return the unique id for this tool
   */
  public abstract String getToolId();

  /**
   * Constructs a {@link JsonObject} from the tool's configuration {@link T} so it can be saved to file.
   *
   * @return the tool's configuration as JSON
   */
  public abstract JsonObject buildToolConfigForSaving();

  /**
   * Called when the tool panel is switched to.
   */
  public void onSwitch() {

  }

  /**
   * Creates an instance of {@link T} configuration for this tool to be used to initialize the tool. Tool configs are
   * found on {@link EditorConfig#toolConfigurations()} using the tool's {@link ToolPanel#getToolId()}.
   *
   * @param editorConfig the {@link EditorConfig} instance
   * @return the configuration object for this tool
   */
  protected abstract T buildToolConfigFromEditorConfig(EditorConfig editorConfig);
}
