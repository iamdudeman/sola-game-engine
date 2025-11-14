package technology.sola.engine.examples.swing;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.Sola;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.platform.swing.SwingSolaPlatform;
import technology.sola.logging.JavaSolaLoggerFactory;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

/**
 * Runs the {@link ExampleLauncherSola} on the {@link SwingSolaPlatform}.
 */
@NullMarked
public class SwingMain {
  static {
    SolaLogger.configure(SolaLogLevel.WARNING, new JavaSolaLoggerFactory());
  }

  /**
   * Entry point for Swing example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SolaPlatform solaPlatform = new SwingSolaPlatform();
    Sola sola = new ExampleLauncherSola();

    solaPlatform.play(sola);
  }
}
