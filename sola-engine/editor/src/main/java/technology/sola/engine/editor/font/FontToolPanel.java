package technology.sola.engine.editor.font;

import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.core.components.ToolPanel;
import technology.sola.engine.editor.core.config.EditorConfig;

public class FontToolPanel extends ToolPanel {
  public FontToolPanel(EditorConfig editorConfig) {
    super(editorConfig);
    var items = getItems();
    var centerPanel = new TabbedPanel();
    var leftPanel = new FontAssetTree(centerPanel);

    items.addAll(leftPanel, centerPanel);
  }

  @Override
  public String getToolLabel() {
    return "Font";
  }

  @Override
  public String getToolId() {
    return "font";
  }
}
