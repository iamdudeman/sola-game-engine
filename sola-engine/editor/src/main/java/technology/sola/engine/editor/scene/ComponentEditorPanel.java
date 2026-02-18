package technology.sola.engine.editor.scene;

import javafx.geometry.Insets;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.components.EditorPanel;

@NullMarked
public class ComponentEditorPanel extends EditorPanel {
  public ComponentEditorPanel() {
    setSpacing(8);
    setPadding(new Insets(4));
  }
}
