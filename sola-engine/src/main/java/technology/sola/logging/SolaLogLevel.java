package technology.sola.logging;

import java.util.logging.Level;

public enum SolaLogLevel {
  OFF(SolaLogger.SolaLevel.OFF),
  INFO(SolaLogger.SolaLevel.INFO),
  WARNING(SolaLogger.SolaLevel.WARNING),
  ERROR(SolaLogger.SolaLevel.ERROR),
  ALL(SolaLogger.SolaLevel.ALL),
  ;

  final int value;

  SolaLogLevel(Level level) {
    this.value = level.intValue();
  }
}
