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
  private final Supplier<Sola> solaSupplier;

  /**
   * Creates a new SolaEditor instance for desired {@link Sola}.
   *
   * @param solaSupplier the method for creating the {@link Sola} instance
   */
  public SolaEditor(Supplier<Sola> solaSupplier) {
    this.solaSupplier = solaSupplier;
  }

  /**
   * Starts the editor.
   */
  public void run() {
    new EditorWindow().show();
  }
}
