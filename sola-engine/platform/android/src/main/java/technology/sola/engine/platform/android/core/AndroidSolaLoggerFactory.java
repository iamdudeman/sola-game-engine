package technology.sola.engine.platform.android.core;

import org.jspecify.annotations.NullMarked;
import technology.sola.logging.JavaSolaLogger;
import technology.sola.logging.SolaLogger;
import technology.sola.logging.SolaLoggerFactory;

import java.util.logging.Logger;

/**
 * AndroidSolaLoggerFactory is a {@link SolaLoggerFactory} implementation for
 * the {@link technology.sola.engine.platform.android.AndroidSolaPlatform}. It will only write to the console.
 */
@NullMarked
public class AndroidSolaLoggerFactory implements SolaLoggerFactory {
  @Override
  public SolaLogger getLogger(Class<?> clazz, String logFile) {
    return new JavaSolaLogger(Logger.getLogger(clazz.getName()));
  }
}
