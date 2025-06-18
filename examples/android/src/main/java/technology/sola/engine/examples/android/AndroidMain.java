package technology.sola.engine.examples.android;

import org.jspecify.annotations.NullMarked;
import technology.sola.logging.JavaSolaLoggerFactory;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

@NullMarked
public class AndroidMain {
  static {
    SolaLogger.configure(SolaLogLevel.WARNING, new JavaSolaLoggerFactory());
  }

  /**
   * Entry point for Swing example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    // SolaPlatform solaPlatform = new SwingSolaPlatform();
    // Sola sola = new ExampleLauncherSola(solaPlatform);

    // solaPlatform.play(sola);
  }
}
