package technology.sola.engine.editor.core.components;

import javafx.scene.control.SplitPane;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.json.JsonObject;

public abstract class ToolPanel<T> extends SplitPane {
  protected final EditorConfig editorConfig;

  public ToolPanel(EditorConfig editorConfig) {
    this.editorConfig = editorConfig;

    setId(getToolId());
  }

  public abstract String getToolLabel();

  public abstract String getToolId();

  public abstract JsonObject buildToolConfigForSaving();

  protected abstract T buildToolConfigFromEditorConfig(EditorConfig editorConfig);
}
