package technology.sola.engine.graphics.guiv2.json.exception;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;

import java.io.Serial;

/**
 * Exception thrown when reading a GUI JSON file that uses a styles property that is not recognized.
 */
public class UnsupportedStylesPropertyException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -5114146342389671283L;

  /**
   * &
   * Creates an instance of this exception for the property key that was not recognized.
   *
   * @param propertyKey     the property key
   * @param baseStylesClass the styles object it was being parsed for
   */
  public UnsupportedStylesPropertyException(String propertyKey, Class<? extends BaseStyles> baseStylesClass) {
    super("Styles property [" + propertyKey + "] is not supported for " + baseStylesClass.getName());
  }
}
