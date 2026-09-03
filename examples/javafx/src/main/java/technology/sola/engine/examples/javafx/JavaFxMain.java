package technology.sola.engine.examples.javafx;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatformConfig;
import technology.sola.logging.JavaSolaLoggerFactory;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

/**
 * Runs the {@link ExampleLauncherSola} on the {@link JavaFxSolaPlatform}.
 */
@NullMarked
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
    var solaPlatform = new JavaFxSolaPlatform(new JavaFxSolaPlatformConfig());
    var sola = new ExampleLauncherSola();

    solaPlatform.play(sola);
  }
}
