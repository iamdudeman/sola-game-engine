package technology.sola.engine.examples.browser;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.platform.browser.BrowserSolaPlatform;
import technology.sola.engine.platform.browser.BrowserSolaPlatformConfig;
import technology.sola.engine.platform.browser.core.BrowserSolaLoggerFactory;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

/**
 * Runs the {@link ExampleLauncherSola} on the {@link BrowserSolaPlatform}.
 */
@NullMarked
public class BrowserMain {
  static {
    SolaLogger.configure(SolaLogLevel.WARNING, new BrowserSolaLoggerFactory());
  }

  /**
   * Entry point for Browser example transpiling.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    var solaPlatform = new BrowserSolaPlatform(new BrowserSolaPlatformConfig());
    var sola = new ExampleLauncherSola();

    solaPlatform.play(sola);
  }
}
