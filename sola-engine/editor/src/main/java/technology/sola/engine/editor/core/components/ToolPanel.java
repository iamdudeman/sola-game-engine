package technology.sola.engine.editor.core.components;

import javafx.scene.control.SplitPane;
import technology.sola.engine.editor.core.config.EditorConfig;

// todo add ability to save/load configuration from EditorConfig per tool

public abstract class ToolPanel extends SplitPane {
  protected final EditorConfig editorConfig;

  public ToolPanel(EditorConfig editorConfig) {
    this.editorConfig = editorConfig;

    setId(getToolId());
  }

  public abstract String getToolLabel();

  public abstract String getToolId();
}
