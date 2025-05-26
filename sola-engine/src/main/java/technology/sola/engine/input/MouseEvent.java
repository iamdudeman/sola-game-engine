package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

/**
 * MouseEvent contains data for a mouse related event.
 *
 * @param button the {@link MouseButton} for the event
 * @param x      the x coordinate of the event
 * @param y      the y coordinate of the event
 */
@NullMarked
public record MouseEvent(MouseButton button, int x, int y) {
  /**
   * Creates a MouseEvent via the {@link MouseButton#getCode()}.
   *
   * @param buttonCode the code of the mouse button for the event
   * @param x          the x coordinate of the event
   * @param y          the y coordinate of the event
   */
  public MouseEvent(int buttonCode, int x, int y) {
    this(MouseButton.valueOf(buttonCode), x, y);
  }
}
