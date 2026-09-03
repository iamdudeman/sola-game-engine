package technology.sola.engine.examples.swing;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.platform.swing.SwingSolaPlatform;
import technology.sola.engine.platform.swing.SwingSolaPlatformConfig;
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
    var solaPlatform = new SwingSolaPlatform(new SwingSolaPlatformConfig());
    var sola = new ExampleLauncherSola();

    solaPlatform.play(sola);
  }
}
