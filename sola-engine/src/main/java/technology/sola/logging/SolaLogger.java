package technology.sola.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SolaLogger instances are used to log messages to the console and/or files. SolaLogger must be configured via
 * {@link SolaLogger#configure(SolaLogLevel, SolaLoggerFactory)} before instances can be created via
 * {@link SolaLogger#of(Class)}. The log level of all logger instances will be the same based on how SolaLogger was
 * configured.
 */
public class SolaLogger {
  private static SolaLogLevel DEFAULT_LEVEL = SolaLogLevel.WARNING;
  private static SolaLoggerFactory solaLoggerFactory;
  private final SolaLogLevel loggerLevel;
  private final Logger logger;

  /**
   * Configures all resulting SolaLoggers to have desired {@link SolaLogLevel} and to be created via desired
   * {@link SolaLoggerFactory}.
   *
   * @param level             the log level for all logger instances
   * @param solaLoggerFactory the factory used to create new logger instances
   */
  public static void configure(SolaLogLevel level, SolaLoggerFactory solaLoggerFactory) {
    SolaLogger.DEFAULT_LEVEL = level;
    SolaLogger.solaLoggerFactory = solaLoggerFactory;
  }

  /**
   * Utilizes the configured {@link SolaLoggerFactory} to create a new {@link SolaLogger} instance for desired class.
   *
   * @param clazz the class to create a logger for
   * @return new logger instance
   */
  public static SolaLogger of(Class<?> clazz) {
    if (solaLoggerFactory == null) {
      throw new IllegalStateException("SolaLogger has not been configured. Call SolaLogger.configure() statically in file with main method.");
    }

    return solaLoggerFactory.getLogger(clazz);
  }

  /**
   * Utilizes the configured {@link SolaLoggerFactory} to create a new {@link SolaLogger} instance for desired class.
   * If the {@link SolaLoggerFactory} creates instances that support writing to a file then the provided log file will
   * be used to write to.
   *
   * @param clazz   the class to create a logger for
   * @param logFile the log file to write to
   * @return new logger instance
   */
  public static SolaLogger of(Class<?> clazz, String logFile) {
    if (solaLoggerFactory == null) {
      throw new IllegalStateException("SolaLogger has not been configured. Call SolaLogger.configure() statically in file with main method.");
    }

    return solaLoggerFactory.getLogger(clazz, logFile);
  }

  /**
   * Manually creates a SolaLogger instance. It will use the configured default {@link SolaLogLevel}. It is recommended
   * to instead utilize the {@link SolaLogger#of(Class)} methods which use the configured {@link SolaLoggerFactory} to
   * create new instances.
   *
   * @param logger the {@link Logger} that powers this SolaLogger
   */
  public SolaLogger(Logger logger) {
    this.logger = logger;

    loggerLevel = DEFAULT_LEVEL;
  }

  /**
   * Logs a message at {@link SolaLogLevel#INFO}.
   *
   * @param message the message to log
   */
  public void info(String message) {
    log(SolaLevel.INFO, message);
  }

  /**
   * Logs a message at {@link SolaLogLevel#INFO}. This uses {@link String#format(String, Object...)} to format the log
   * message.
   *
   * @param message the message to log
   * @param params the additional params for the log message
   */
  public void info(String message, Object... params) {
    log(SolaLevel.INFO, message, params);
  }

  /**
   * Logs a message at {@link SolaLogLevel#WARNING}.
   *
   * @param message the message to log
   */
  public void warning(String message) {
    log(SolaLevel.WARNING, message);
  }

  /**
   * Logs a message at {@link SolaLogLevel#WARNING}. This uses {@link String#format(String, Object...)} to format the
   * log message.
   *
   * @param message the message to log
   * @param params the additional params for the log message
   */
  public void warning(String message, Object... params) {
    log(SolaLevel.WARNING, message, params);
  }

  /**
   * Logs a message at {@link SolaLogLevel#ERROR}.
   *
   * @param message the message to log
   */
  public void error(String message) {
    log(SolaLevel.ERROR, message);
  }

  /**
   * Logs a message at {@link SolaLogLevel#ERROR}. Additionally, the passed {@link Throwable} will be logged.
   *
   * @param message the message to log
   * @param throwable the exception that should be logged
   */
  public void error(String message, Throwable throwable) {
    log(SolaLevel.ERROR, message, throwable);
  }

  /**
   * Logs a message at {@link SolaLogLevel#ERROR}. This uses {@link String#format(String, Object...)} to format the
   * log message. Additionally, the passed {@link Throwable} will be logged.
   *
   * @param message the message to log
   * @param throwable the exception that should be logged
   * @param params the additional params for the log message
   */
  public void error(String message, Throwable throwable, Object... params) {
    log(SolaLevel.ERROR, message, throwable, params);
  }

  private void log(Level level, String message, Object... params) {
    if (isNotLoggable(level)) {
      return;
    }

    logger.log(level, () -> String.format(message, params));
  }

  private void log(Level level, String message) {
    if (isNotLoggable(level)) {
      return;
    }

    logger.log(level, message);
  }

  private void log(Level level, String message, Throwable throwable, Object... params) {
    if (isNotLoggable(level)) {
      return;
    }

    if (throwable == null) {
      logger.log(level, () -> String.format(message, params));
    } else {
      logger.log(level, throwable, () -> String.format(message, params));
    }
  }

  private boolean isNotLoggable(Level level) {
    int levelValue = this.loggerLevel.value;

    return level.intValue() < levelValue || levelValue == SolaLogLevel.OFF.value;
  }

  static class SolaLevel extends Level {
    static final SolaLevel ERROR = new SolaLevel("ERROR", Level.SEVERE.intValue());

    protected SolaLevel(String name, int value) {
      super(name, value);
    }
  }
}
