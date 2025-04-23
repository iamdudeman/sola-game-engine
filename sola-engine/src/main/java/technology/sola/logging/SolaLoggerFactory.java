package technology.sola.logging;

public interface SolaLoggerFactory {
  SolaLogger getLogger(Class<?> clazz, String logFile);

  default SolaLogger getLogger(Class<?> clazz) {
    return getLogger(clazz, "logs/sola.log");
  }
}
