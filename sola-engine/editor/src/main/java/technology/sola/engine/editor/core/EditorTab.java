package technology.sola.engine.editor.core;

import javafx.scene.layout.Region;

record EditorTab(
  String id,
  String label,
  Region leftPanel,
  Region centerPanel,
  Region rightPanel,
  Region bottomPanel
) {
}
