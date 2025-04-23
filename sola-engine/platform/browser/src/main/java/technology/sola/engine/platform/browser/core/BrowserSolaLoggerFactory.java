package technology.sola.engine.platform.browser.core;

import technology.sola.logging.SolaLogger;
import technology.sola.logging.SolaLoggerFactory;

import java.util.logging.Logger;

/**
 * BrowserSolaLoggerFactory is a {@link SolaLoggerFactory} implementation for
 * the {@link technology.sola.engine.platform.browser.BrowserSolaPlatform}.
 */
public class BrowserSolaLoggerFactory implements SolaLoggerFactory {
  @Override
  public SolaLogger getLogger(Class<?> clazz, String logFile) {
    return new SolaLogger(Logger.getLogger(clazz.getName()));
  }
}
