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
  private final SolaEditorCustomization solaEditorCustomization;

  /**
   * Creates a new SolaEditor instance for desired {@link Sola}.
   *
   * @param solaSupplier            the method for creating the {@link Sola} instance
   * @param solaEditorCustomization the customization for the editor
   */
  public SolaEditor(Supplier<Sola> solaSupplier, SolaEditorCustomization solaEditorCustomization) {
    this.solaSupplier = solaSupplier;
    this.solaEditorCustomization = solaEditorCustomization;
  }

  /**
   * Starts the editor.
   */
  public void run() {
    new EditorWindow(solaEditorCustomization).show();
  }
}
