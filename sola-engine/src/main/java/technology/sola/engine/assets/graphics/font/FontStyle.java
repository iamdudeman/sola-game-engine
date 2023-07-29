package technology.sola.engine.assets.graphics.font;

/**
 * FontStyle is an enum of possible {@link Font} style options. These are based on the
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/awt/Font.html">java.awt.Font</a> style options.
 */
public enum FontStyle {
  /**
   * Normal/plain styling.
   */
  NORMAL(0, "Normal"),
  /**
   * Italic styling.
   */
  ITALIC(1, "Italic"),
  /**
   * Bold styling.
   */
  BOLD(2, "Bold"),
  /**
   * Bold and italic styling.
   */
  BOLD_ITALIC(3, "Bold-italic"),
  ;

  private final int code;
  private final String name;

  FontStyle(int code, String name) {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the integer representation of the font style.
   *
   * @return the font style code
   */
  public int getCode() {
    return code;
  }

  /**
   * @return the name of the font style
   */
  public String getName() {
    return name;
  }
}
