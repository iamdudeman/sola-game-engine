package technology.sola.engine.platform.android.core;

import android.util.Log;
import org.jspecify.annotations.NullMarked;
import technology.sola.logging.JavaSolaLogger;
import technology.sola.logging.SolaLogger;
import technology.sola.logging.SolaLoggerFactory;

import java.util.logging.*;

/**
 * AndroidSolaLoggerFactory is a {@link SolaLoggerFactory} implementation for
 * the {@link technology.sola.engine.platform.android.AndroidSolaPlatform}. It will only write to the console.
 */
@NullMarked
public class AndroidSolaLoggerFactory implements SolaLoggerFactory {
  @Override
  public SolaLogger getLogger(Class<?> clazz, String logFile) {
    return new JavaSolaLogger(initLogger(clazz, logFile));
  }

  private static Logger initLogger(Class<?> clazz, String logFile) {
    Logger logger = Logger.getLogger(clazz.getName());

    logger.addHandler(new AndroidLoggingHandler());
    logger.setUseParentHandlers(false);

    return logger;
  }


  private static class AndroidLoggingHandler extends Handler {
    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(LogRecord record) {
      if (!super.isLoggable(record))
        return;

      String name = record.getLoggerName();
      int maxLength = 30;
      String tag = name;

      if (tag.length() > maxLength) {
        tag = tag.substring(tag.lastIndexOf(".") + 1);
      }

      try {
        int level = getAndroidLevel(record.getLevel());

        Log.println(level, tag, record.getMessage());

        if (record.getThrown() != null) {
          Log.println(level, tag, Log.getStackTraceString(record.getThrown()));
        }
      } catch (RuntimeException e) {
        Log.e("AndroidLoggingHandler", "Error logging message.", e);
      }
    }

    static int getAndroidLevel(Level level) {
      int value = level.intValue();

      if (value >= Level.SEVERE.intValue()) {
        return Log.ERROR;
      } else if (value >= Level.WARNING.intValue()) {
        return Log.WARN;
      } else if (value >= Level.INFO.intValue()) {
        return Log.INFO;
      } else {
        return Log.DEBUG;
      }
    }
  }
}
