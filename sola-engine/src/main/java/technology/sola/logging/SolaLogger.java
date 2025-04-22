package technology.sola.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.*;
import java.util.stream.Collectors;

// todo abstract out LogLevel from java.util.logging
// todo don't read logLevel as a property
// todo ensure this works with browser/teavm

public class SolaLogger {
  private static final String LOG_FORMAT = "[%s] [%-7s] [%s] %s %n";
  private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final long STACKTRACE_LIMIT = 5;

  static final String LOG_LEVEL_PROPERTY = "logLevel";

  private static FileHandler errorLogHandler = null;

  private final Logger logger;

  public static void setAllLogLevels(Level level) {
    Logger.getLogger("").setLevel(level);
  }

  public static SolaLogger of(Class clazz) {
    return new SolaLogger(initLogger(clazz));
  }

  public void info(String message) {
    logger.info(message);
  }

  public void warning(String message) {
    logger.warning(message);
  }

  public void error(String message) {
    logger.severe(message);
  }

  public void exception(Throwable throwable) {
    exception("", throwable);
  }

  public void exception(String message, Throwable throwable) {
    if (logger.isLoggable(Level.SEVERE)) {
      String stackTrace =
        Arrays.stream(throwable.getStackTrace())
          .limit(STACKTRACE_LIMIT)
          .map(StackTraceElement::toString)
          .collect(Collectors.joining("\n\t"));

      logger.log(Level.SEVERE, message + "\n\t" + throwable.getMessage() + "\n\t" + stackTrace);
    }
  }

  private SolaLogger(Logger logger) {
    this.logger = logger;
  }

  private static java.util.logging.Logger initLogger(Class clazz) {
    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(clazz.getName());

    Level level = getLogLevel();
    logger.setLevel(level);

    try {
      if (!Files.exists(Paths.get("logs"))) {
        Files.createDirectory(Paths.get("logs"));
      }
      if (errorLogHandler == null) {
        errorLogHandler = initErrorLogHandler(level);
      }

      logger.addHandler(errorLogHandler);
      logger.setUseParentHandlers(false);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, ex.getMessage());
    }

    return logger;
  }

  private static FileHandler initErrorLogHandler(Level level) throws IOException {
    FileHandler fileHandler = new FileHandler("logs/solkana.log", 0, 1, true);
    fileHandler.setLevel(level);
    fileHandler.setFormatter(new SimpleFormatter() {
      private SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);

      @Override
      public synchronized String format(LogRecord lr) {
        return String.format(LOG_FORMAT,
          dateFormat.format(new Date(lr.getMillis())),
          lr.getLevel().getName(),
          lr.getSourceClassName(),
          lr.getMessage()
        );
      }
    });

    return fileHandler;
  }

  private static Level getLogLevel() {
    try {
      return Level.parse(System.getProperty(LOG_LEVEL_PROPERTY, Level.SEVERE.getName()));
    } catch (IllegalArgumentException ex) {
      return Level.SEVERE;
    }
  }
}
