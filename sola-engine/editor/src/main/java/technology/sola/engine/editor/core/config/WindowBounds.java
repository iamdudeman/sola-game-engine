package technology.sola.engine.editor.core.config;

/**
 * WindowBounds contains the top, left coordinate of the editor window and its width and height.
 *
 * @param x      the left most coordinate
 * @param y      the top most coordinate
 * @param width  the width of the window
 * @param height the height of the window
 */
public record WindowBounds(int x, int y, int width, int height) {
  /**
   * Creates an instance with default values set.
   */
  public WindowBounds() {
    this(12, 12, 1200, 800);
  }
}
