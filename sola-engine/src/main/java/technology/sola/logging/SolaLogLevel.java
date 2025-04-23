package technology.sola.logging;

import java.util.logging.Level;

/**
 * SolaLogLevel defines the available sola engine logging levels.
 */
public enum SolaLogLevel {
  /**
   * OFF is a special level that can be used to turn off logging.
   */
  OFF(SolaLogger.SolaLevel.OFF),
  /**
   * INFO is a message level for general information that may or may not be useful depending on the context.
   */
  INFO(SolaLogger.SolaLevel.INFO),
  /**
   * WARNING is a message level indicating a potential problem.
   */
  WARNING(SolaLogger.SolaLevel.WARNING),
  /**
   * ERROR is a message level indicating a serious failure.
   */
  ERROR(SolaLogger.SolaLevel.ERROR),
  ;

  final int value;

  SolaLogLevel(Level level) {
    this.value = level.intValue();
  }
}
