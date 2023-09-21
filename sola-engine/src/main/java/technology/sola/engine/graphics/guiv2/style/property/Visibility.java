package technology.sola.engine.graphics.guiv2.style.property;

/**
 * Visibility contains options for element rendering that do not change layout.
 */
public enum Visibility {
  /**
   * Element will not render, and it will not take up any space in the layout.
   */
  NONE,
  /**
   * Element with render normally.
   */
  VISIBLE,
  /**
   * Element will not render, but it will continue to take up its space in the layout.
   */
  HIDDEN
}
