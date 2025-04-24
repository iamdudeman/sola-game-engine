package technology.sola.logging;

import java.util.logging.Level;

/**
 * SolaLogLevel defines the available sola engine logging levels.
 */
public enum SolaLogLevel {
  /**
   * OFF is a special level that can be used to turn off logging.
   */
  OFF(SolaLevel.OFF),
  /**
   * INFO is a message level for general information that may or may not be useful depending on the context.
   */
  INFO(SolaLevel.INFO),
  /**
   * WARNING is a message level indicating a potential problem.
   */
  WARNING(SolaLevel.WARNING),
  /**
   * ERROR is a message level indicating a serious failure.
   */
  ERROR(SolaLevel.ERROR),
  ;

  final Level level;

  SolaLogLevel(Level level) {
    this.level = level;
  }

  private static class SolaLevel extends Level {
    static final SolaLevel ERROR = new SolaLevel("ERROR", Level.SEVERE.intValue());

    protected SolaLevel(String name, int value) {
      super(name, value);
    }
  }
}
