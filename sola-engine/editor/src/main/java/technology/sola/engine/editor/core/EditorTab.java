package technology.sola.engine.editor.core;

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import technology.sola.engine.editor.core.components.EditorPanel;
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
    this(label, new EditorPanel(new VBox(new Text("Blank"))), new TabbedPanel(), new EditorPanel(new VBox(new Text("Blank"))), new EditorPanel(new VBox(new Text("Blank"))));
  }

  // todo for early development
  @Deprecated
  public EditorTab(String label, Region leftPanel) {
    this(label, leftPanel, new TabbedPanel(), new EditorPanel(new VBox(new Text("Blank"))), new EditorPanel(new VBox(new Text("Blank"))));
  }
}
