package technology.sola.engine.editor.core;

import javafx.scene.layout.Region;

record EditorTab(
  String label,
  Region leftPanel,
  Region centerPanel,
  Region rightPanel,
  Region bottomPanel
) {
}
