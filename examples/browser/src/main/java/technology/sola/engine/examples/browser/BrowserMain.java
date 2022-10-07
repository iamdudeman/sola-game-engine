package technology.sola.engine.examples.browser;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.Sola;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.platform.browser.BrowserSolaPlatform;

/**
 * Runs the {@link ExampleLauncherSola} on the {@link BrowserSolaPlatform}.
 */
public class BrowserMain {
  /**
   * Entry point for Browser example transpiling.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SolaPlatform solaPlatform = new BrowserSolaPlatform();
    Sola sola = new ExampleLauncherSola(solaPlatform);

    solaPlatform.play(sola);
  }
}
