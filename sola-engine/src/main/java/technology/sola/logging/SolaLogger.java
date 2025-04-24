package technology.sola.logging;

/**
 * SolaLogger instances are used to log messages to the console and/or files. SolaLogger must be configured via
 * {@link SolaLogger#configure(SolaLogLevel, SolaLoggerFactory)} before instances can be created via
 * {@link SolaLogger#of(Class)}. The log level of all logger instances will be the same based on how SolaLogger was
 * configured.
 */
public abstract class SolaLogger {
  private static SolaLogLevel defaultLoggerLevel = SolaLogLevel.WARNING;
  private static SolaLoggerFactory solaLoggerFactory;
  private final SolaLogLevel loggerLevel = defaultLoggerLevel;

  /**
   * Configures all resulting SolaLoggers to have desired {@link SolaLogLevel} and to be created via desired
   * {@link SolaLoggerFactory}.
   *
   * @param level             the log level for all logger instances
   * @param solaLoggerFactory the factory used to create new logger instances
   */
  public static void configure(SolaLogLevel level, SolaLoggerFactory solaLoggerFactory) {
    SolaLogger.defaultLoggerLevel = level;
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
   * Logs a message at {@link SolaLogLevel#INFO}.
   *
   * @param message the message to log
   */
  public void info(String message) {
    if (isLoggable(SolaLogLevel.INFO)) {
      log(SolaLogLevel.INFO, message, null);
    }
  }

  /**
   * Logs a message at {@link SolaLogLevel#INFO}. This uses {@link String#format(String, Object...)} to format the log
   * message.
   *
   * @param message the message to log
   * @param params  the additional params for the log message
   */
  public void info(String message, Object... params) {
    if (isLoggable(SolaLogLevel.INFO)) {
      log(SolaLogLevel.INFO, message, null, params);
    }
  }

  /**
   * Logs a message at {@link SolaLogLevel#WARNING}.
   *
   * @param message the message to log
   */
  public void warning(String message) {
    if (isLoggable(SolaLogLevel.WARNING)) {
      log(SolaLogLevel.WARNING, message, null);
    }
  }

  /**
   * Logs a message at {@link SolaLogLevel#WARNING}. This uses {@link String#format(String, Object...)} to format the
   * log message.
   *
   * @param message the message to log
   * @param params  the additional params for the log message
   */
  public void warning(String message, Object... params) {
    if (isLoggable(SolaLogLevel.WARNING)) {
      log(SolaLogLevel.WARNING, message, null, params);
    }
  }

  /**
   * Logs a message at {@link SolaLogLevel#ERROR}.
   *
   * @param message the message to log
   */
  public void error(String message) {
    if (isLoggable(SolaLogLevel.ERROR)) {
      log(SolaLogLevel.ERROR, message, null);
    }
  }

  /**
   * Logs a message at {@link SolaLogLevel#ERROR}. Additionally, the passed {@link Throwable} will be logged.
   *
   * @param message   the message to log
   * @param throwable the exception that should be logged
   */
  public void error(String message, Throwable throwable) {
    if (isLoggable(SolaLogLevel.ERROR)) {
      log(SolaLogLevel.ERROR, message, throwable);
    }
  }

  /**
   * Logs a message at {@link SolaLogLevel#ERROR}. This uses {@link String#format(String, Object...)} to format the
   * log message. Additionally, the passed {@link Throwable} will be logged.
   *
   * @param message   the message to log
   * @param throwable the exception that should be logged
   * @param params    the additional params for the log message
   */
  public void error(String message, Throwable throwable, Object... params) {
    if (isLoggable(SolaLogLevel.ERROR)) {
      log(SolaLogLevel.ERROR, message, throwable, params);
    }
  }

  /**
   * Logs a message at desired {@link SolaLogLevel}. This uses {@link String#format(String, Object...)} to format the
   * log message. Additionally, the passed {@link Throwable} will be logged if provided.
   *
   * @param level     the {@link SolaLogLevel} to log at
   * @param message   the message to log
   * @param throwable the exception that should be logged
   * @param params    the additional params for the log message
   */
  protected abstract void log(SolaLogLevel level, String message, Throwable throwable, Object... params);

  private boolean isLoggable(SolaLogLevel logRecordLevel) {
    int levelValue = loggerLevel.level.intValue();

    return logRecordLevel.level.intValue() >= levelValue && levelValue != SolaLogLevel.OFF.level.intValue();
  }
}
