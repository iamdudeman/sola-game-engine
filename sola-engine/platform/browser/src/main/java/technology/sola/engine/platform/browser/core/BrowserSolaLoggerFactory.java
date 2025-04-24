package technology.sola.engine.platform.browser.core;

import technology.sola.logging.SolaLogger;
import technology.sola.logging.SolaLoggerFactory;
import technology.sola.logging.JavaSolaLogger;

import java.util.logging.Logger;

/**
 * BrowserSolaLoggerFactory is a {@link SolaLoggerFactory} implementation for
 * the {@link technology.sola.engine.platform.browser.BrowserSolaPlatform}. It will only write to the browser console.
 */
public class BrowserSolaLoggerFactory implements SolaLoggerFactory {
  @Override
  public SolaLogger getLogger(Class<?> clazz, String logFile) {
    return new JavaSolaLogger(Logger.getLogger(clazz.getName()));
  }
}
