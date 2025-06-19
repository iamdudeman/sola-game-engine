package technology.sola.engine.examples.android;

import technology.sola.logging.JavaSolaLoggerFactory;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

public class MainActivity extends TempActivity {
  static {
    // todo might need AndroidSolaLoggerFactory
    SolaLogger.configure(SolaLogLevel.WARNING, new JavaSolaLoggerFactory());
  }
}
