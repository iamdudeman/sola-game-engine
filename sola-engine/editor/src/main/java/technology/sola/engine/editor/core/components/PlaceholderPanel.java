package technology.sola.engine.editor.core.components;

import javafx.scene.layout.VBox;

/**
 * Temporary panel for placeholder content.
 */
@Deprecated
public class PlaceholderPanel extends EditorPanel {
  @Deprecated
  public PlaceholderPanel() {
    super(new VBox(new ThemedText("Placeholder")));
  }
}
