package technology.sola.logging;

/**
 * SolaLoggerFactory is an interface for defining the api for creating new {@link SolaLogger} instances for a target
 * {@link technology.sola.engine.core.SolaPlatform}.
 */
public interface SolaLoggerFactory {
  /**
   * The default log file to use if not provided.
   */
  String DEFAULT_LOG_FILE = "logs/sola.log";

  /**
   * Creates a new {@link SolaLogger} for desired {@link Class}. The logs for the class will be in desired logFile.
   *
   * @param clazz   the class the logger is for
   * @param logFile the output log file
   * @return new logger instance
   */
  SolaLogger getLogger(Class<?> clazz, String logFile);

  /**
   * Creates a new {@link SolaLogger} for desired {@link Class}. The default log
   * file {@link SolaLoggerFactory#DEFAULT_LOG_FILE} will be used.
   *
   * @param clazz the class the logger is for
   * @return new logger instance
   */
  default SolaLogger getLogger(Class<?> clazz) {
    return getLogger(clazz, DEFAULT_LOG_FILE);
  }
}
