package technology.sola.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class SolaLogMessageFormatter extends SimpleFormatter {
  private static final String LOG_FORMAT = "[%s] [%-7s] [%s] %s %n";
  private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private final SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);

  @Override
  public synchronized String format(LogRecord logRecord) {
    return String.format(LOG_FORMAT,
      dateFormat.format(new Date(logRecord.getMillis())),
      logRecord.getLevel().getName(),
      logRecord.getLoggerName(),
      logRecord.getMessage()
    );
  }
}
