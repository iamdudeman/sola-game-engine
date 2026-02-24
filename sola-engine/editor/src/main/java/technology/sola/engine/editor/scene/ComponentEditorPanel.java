package technology.sola.engine.editor.scene;

import javafx.geometry.Insets;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.components.EditorPanel;

/**
 * ComponentEditorPanel is an {@link EditorPanel} for editing components.
 */
@NullMarked
public class ComponentEditorPanel extends EditorPanel {
  /**
   * Creates an instance of this panel.
   */
  public ComponentEditorPanel() {
    setSpacing(8);
    setPadding(new Insets(4));
  }
}
