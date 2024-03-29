package technology.sola.engine.examples.swing;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.Sola;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.platform.swing.SwingSolaPlatform;

/**
 * Runs the {@link ExampleLauncherSola} on the {@link SwingSolaPlatform}.
 */
public class SwingMain {
  /**
   * Entry point for Swing example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SolaPlatform solaPlatform = new SwingSolaPlatform();
    Sola sola = new ExampleLauncherSola(solaPlatform);

    solaPlatform.play(sola);
  }
}
