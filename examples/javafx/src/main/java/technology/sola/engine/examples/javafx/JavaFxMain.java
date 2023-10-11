package technology.sola.engine.examples.javafx;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.examples.common.singlefile.GuiV2Example;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;

/**
 * Runs the {@link ExampleLauncherSola} on the {@link JavaFxSolaPlatform}.
 */
public class JavaFxMain {
  /**
   * Entry point for JavaFX example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SolaPlatform solaPlatform = new JavaFxSolaPlatform();
    // Sola sola = new ExampleLauncherSola(solaPlatform); // todo uncomment this before merging
    Sola sola = new GuiV2Example();


    solaPlatform.play(sola);
  }
}
