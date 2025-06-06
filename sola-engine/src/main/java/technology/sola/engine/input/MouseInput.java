package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.math.linear.Vector2D;

import java.util.EnumMap;
import java.util.Map;

/**
 * MouseInput contains information about user interaction with the mouse.
 */
@NullMarked
public class MouseInput {
  private final Map<MouseButton, Boolean> mouseDownMap = new EnumMap<>(MouseButton.class);
  private final Map<MouseButton, ButtonState> mouseStatusMap = new EnumMap<>(MouseButton.class);
  private Vector2D lastEventPosition = new Vector2D(0, 0);
  @Nullable
  private Vector2D currentMousePosition = null;
  private MouseWheelEvent lastMouseWheelEvent = MouseWheelEvent.NONE;
  private MouseWheelEvent currentMouseWheelEvent = MouseWheelEvent.NONE;

  /**
   * Checks if a {@link MouseButton} is pressed or not.
   *
   * @param mouseButton the button to check
   * @return true if mouse button is pressed
   */
  public boolean isMousePressed(MouseButton mouseButton) {
    return mouseStatusMap.get(mouseButton) == ButtonState.PRESSED;
  }

  /**
   * Checks if a {@link MouseButton} is being dragged or not.
   *
   * @param mouseButton the button to check
   * @return true if mouse button is being dragged
   */
  public boolean isMouseDragged(MouseButton mouseButton) {
    return mouseStatusMap.get(mouseButton) == ButtonState.DRAGGED;
  }

  /**
   * @return the current position of the mouse on screen
   */
  @Nullable
  public Vector2D getMousePosition() {
    return currentMousePosition;
  }

  /**
   * Returns the last {@link MouseWheelEvent} that was received. Defaults to {@link MouseWheelEvent#NONE} if there was
   * no mouse wheel event this frame.
   *
   * @return the last mouse wheel event
   */
  public MouseWheelEvent getMouseWheel() {
    return currentMouseWheelEvent;
  }

  /**
   * Called once per frame to update the current status of the mouse based on the user's interaction.
   */
  public void updateStatusOfMouse() {
    // update mouse position
    currentMousePosition = lastEventPosition;

    // update mouse wheel
    currentMouseWheelEvent = lastMouseWheelEvent;
    lastMouseWheelEvent = MouseWheelEvent.NONE;

    // update mouse button states
    mouseDownMap.forEach((mouseButton, isDown) -> {
      if (isDown) {
        ButtonState buttonState = mouseStatusMap.getOrDefault(mouseButton, ButtonState.RELEASED);

        if (buttonState == ButtonState.PRESSED) {
          mouseStatusMap.put(mouseButton, ButtonState.DRAGGED);
        } else if (buttonState == ButtonState.RELEASED) {
          mouseStatusMap.put(mouseButton, ButtonState.PRESSED);
        }
      } else {
        mouseStatusMap.put(mouseButton, ButtonState.RELEASED);
      }
    });
  }

  /**
   * Called on move of the mouse.
   *
   * @param mouseEvent the {@link MouseEvent}
   */
  public void onMouseMoved(MouseEvent mouseEvent) {
    updateMousePosition(mouseEvent);
  }

  /**
   * Called on press of a mouse button.
   *
   * @param mouseEvent the {@link MouseEvent}
   */
  public void onMousePressed(MouseEvent mouseEvent) {
    updateMousePosition(mouseEvent);
    mouseDownMap.put(mouseEvent.button(), true);
  }

  /**
   * Called on release of a mouse button.
   *
   * @param mouseEvent the {@link MouseEvent}
   */
  public void onMouseReleased(MouseEvent mouseEvent) {
    updateMousePosition(mouseEvent);
    mouseDownMap.put(mouseEvent.button(), false);
  }

  /**
   * Captures the last {@link MouseWheelEvent} that happened.
   *
   * @param mouseWheelEvent the last mouse wheel event that happened
   */
  public void onMouseWheel(MouseWheelEvent mouseWheelEvent) {
    lastMouseWheelEvent = mouseWheelEvent;
  }

  private void updateMousePosition(MouseEvent mouseEvent) {
    lastEventPosition = new Vector2D((float) mouseEvent.x(), (float) mouseEvent.y());
  }

  private enum ButtonState {
    DRAGGED,
    PRESSED,
    RELEASED
  }
}
