package technology.sola.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SolaLogger {
  private static SolaLogLevel DEFAULT_LEVEL = SolaLogLevel.WARNING;
  private static SolaLoggerFactory solaLoggerFactory;
  private SolaLogLevel loggerLevel = SolaLogLevel.ERROR;
  private final Logger logger;

  public static void configure(SolaLogLevel level, SolaLoggerFactory solaLoggerFactory) {
    SolaLogger.DEFAULT_LEVEL = level;
    SolaLogger.solaLoggerFactory = solaLoggerFactory;
  }

  public static void configure(SolaLogLevel level) {
    SolaLogger.DEFAULT_LEVEL = level;
    SolaLogger.solaLoggerFactory = new JavaSolaLoggerFactory();
  }

  public static SolaLogger of(Class<?> clazz) {
    if (solaLoggerFactory == null) {
      throw new IllegalStateException("SolaLogger has not been configured. Call SolaLogger.configure() statically in file with main method.");
    }

    var logger = solaLoggerFactory.getLogger(clazz);

    logger.loggerLevel = DEFAULT_LEVEL;

    return logger;
  }

  public static SolaLogger of(Class<?> clazz, String logFile) {
    if (solaLoggerFactory == null) {
      throw new IllegalStateException("SolaLogger has not been configured. Call SolaLogger.configure() statically in file with main method.");
    }

    var logger = solaLoggerFactory.getLogger(clazz, logFile);

    logger.loggerLevel = DEFAULT_LEVEL;

    return logger;
  }

  public SolaLogger(Logger logger) {
    this.logger = logger;
  }

  public void info(String message) {
    log(SolaLevel.INFO, message);
  }

  public void info(String message, Object... params) {
    log(SolaLevel.INFO, message, params);
  }

  public void warning(String message) {
    log(SolaLevel.WARNING, message);
  }

  public void warning(String message, Object... params) {
    log(SolaLevel.WARNING, message, params);
  }

  public void error(String message) {
    log(SolaLevel.ERROR, message);
  }

  public void error(String message, Throwable throwable) {
    log(SolaLevel.ERROR, message, throwable);
  }

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
