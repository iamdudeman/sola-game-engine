package technology.sola.engine.editor.scene;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.styles.Css;

/**
 * ComponentEditorPanel is an {@link EditorPanel} for editing components.
 */
@NullMarked
public class ComponentEditorPanel extends EditorPanel {
  /**
   * Creates an instance of this panel.
   */
  public ComponentEditorPanel() {
    getStyleClass().add(Css.Util.SPACING_4X);
  }
}
