package technology.sola.engine.graphics.guiv2.json.exception;

/**
 * Exception thrown when reading a GUI JSON file that uses a styles property that is not recognized.
 */
public class UnsupportedStylesPropertyException extends RuntimeException {
  /**
   * Creates an instance of this exception.
   *
   * @param propertyKey the unsupported property key
   */
  public UnsupportedStylesPropertyException(String propertyKey) {
    super("Styles property [" + propertyKey + "] not supported");
  }
}
