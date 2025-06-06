package technology.sola.engine.editor.core;

import javafx.scene.Parent;
import javafx.scene.Scene;
import org.jspecify.annotations.NullMarked;

/**
 * EditorScene is a {@link Scene} that has commonly used css files added to it.
 */
@NullMarked
public class EditorScene extends Scene {
  /**
   * Creates an instance of the editor scene.
   *
   * @param root the root {@link Parent} for the scene
   */
  public EditorScene(Parent root) {
    super(root);

    getStylesheets().add("utility-styles.css");
  }
}
