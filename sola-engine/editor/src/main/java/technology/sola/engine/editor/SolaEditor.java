package technology.sola.engine.editor;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.Sola;
import technology.sola.engine.editor.core.EditorWindow;

import java.util.function.Supplier;

/**
 * SolaEditor holds the configuration for an instance of the sola game engine editor and the method to start the editor.
 */
@NullMarked
public class SolaEditor {
  // todo method to build Sola instance -> ExampleSola::new
  private Supplier<Sola> solaSupplier;

  /**
   * Starts the editor.
   */
  public void run() {
    new EditorWindow().show();
  }
}
