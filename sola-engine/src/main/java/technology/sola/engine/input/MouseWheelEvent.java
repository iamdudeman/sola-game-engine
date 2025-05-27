package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

/**
 * MouseWheelEvent is an event object containing information about an interaction with the mouse wheel.
 *
 * @param isUp    true if the mouse wheel was moved up
 * @param isDown  true if the mouse wheel was moved down
 * @param isLeft  true if the mouse wheel was moved left
 * @param isRight true if the mouse wheel was moved right
 */
@NullMarked
public record MouseWheelEvent(
  boolean isUp,
  boolean isDown,
  boolean isLeft,
  boolean isRight
) {
  /**
   * MouseWheelEvent to represent when no interaction with the mouse wheel has taken place.
   */
  public static final MouseWheelEvent NONE = new MouseWheelEvent(false, false, false, false);
}
