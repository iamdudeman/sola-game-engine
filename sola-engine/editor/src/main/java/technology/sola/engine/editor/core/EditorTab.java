package technology.sola.engine.editor.core;

import javafx.scene.layout.Region;
import technology.sola.engine.editor.core.components.PlaceholderPanel;
import technology.sola.engine.editor.core.components.TabbedPanel;

public record EditorTab(
  String label,
  Region leftPanel,
  Region centerPanel,
  Region rightPanel,
  Region bottomPanel
) {
  // todo for early development
  @Deprecated
  public EditorTab(String label) {
    this(label, new PlaceholderPanel(), new TabbedPanel(), new PlaceholderPanel(), new PlaceholderPanel());
  }

  // todo for early development
  @Deprecated
  public EditorTab(String label, Region leftPanel) {
    this(label, leftPanel, new TabbedPanel(), new PlaceholderPanel(), new PlaceholderPanel());
  }
}
