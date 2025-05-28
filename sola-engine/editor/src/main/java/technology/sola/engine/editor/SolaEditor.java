package technology.sola.engine.editor;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.EditorWindow;

/**
 * SolaEditor holds the configuration for an instance of the sola game engine editor and the method to start the editor.
 */
@NullMarked
public class SolaEditor {
  /**
   * Starts the editor.
   */
  public void run() {
    new EditorWindow().show();
  }
}
