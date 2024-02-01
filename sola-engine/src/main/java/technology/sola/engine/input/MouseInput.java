package technology.sola.engine.input;

import technology.sola.math.linear.Vector2D;

import java.util.EnumMap;
import java.util.Map;

/**
 * MouseInput contains information about user interaction with the mouse.
 */
public class MouseInput {
  private final Map<MouseButton, Boolean> mouseDownMap = new EnumMap<>(MouseButton.class);
  private final Map<MouseButton, ButtonState> mouseStatusMap = new EnumMap<>(MouseButton.class);
  private Vector2D lastEventPosition = new Vector2D(0, 0);
  private Vector2D currentMousePosition = null;

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
  public Vector2D getMousePosition() {
    return currentMousePosition;
  }

  /**
   * Called once per frame to update the current status of the mouse based on the user's interaction.
   */
  public void updateStatusOfMouse() {
    currentMousePosition = lastEventPosition;

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

  private void updateMousePosition(MouseEvent mouseEvent) {
    lastEventPosition = new Vector2D((float) mouseEvent.x(), (float) mouseEvent.y());
  }

  private enum ButtonState {
    DRAGGED,
    PRESSED,
    RELEASED
  }
}
