package technology.sola.engine.examples.javafx;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;
import technology.sola.logging.JavaSolaLoggerFactory;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

/**
 * Runs the {@link ExampleLauncherSola} on the {@link JavaFxSolaPlatform}.
 */
public class JavaFxMain {
  static {
    SolaLogger.configure(SolaLogLevel.WARNING, new JavaSolaLoggerFactory());
  }

  /**
   * Entry point for JavaFX example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SolaPlatform solaPlatform = new JavaFxSolaPlatform();
    Sola sola = new ExampleLauncherSola(solaPlatform);

    solaPlatform.play(sola);
  }
}
