package technology.sola.logging;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.logging.Logger;

/**
 * JavaSolaLogger is a {@link SolaLogger} implementation powered by {@link java.util.logging.Logger}.
 */
@NullMarked
public class JavaSolaLogger extends SolaLogger {
  private final Logger logger;

  /**
   * Creates a JavaSolaLogger instance.
   *
   * @param logger the {@link Logger} that powers this SolaLogger
   */
  public JavaSolaLogger(Logger logger) {
    this.logger = logger;
  }

  @Override
  protected void log(SolaLogLevel level, String message, @Nullable Throwable throwable, Object... params) {
    logger.log(level.level, throwable, () -> String.format(message, params));
  }
}
