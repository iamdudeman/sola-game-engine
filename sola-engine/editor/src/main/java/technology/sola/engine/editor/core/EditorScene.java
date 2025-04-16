package technology.sola.engine.editor.core;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class EditorScene extends Scene {
  public EditorScene(Parent root) {
    super(root);

    getStylesheets().add("utility-styles.css");
  }
}
