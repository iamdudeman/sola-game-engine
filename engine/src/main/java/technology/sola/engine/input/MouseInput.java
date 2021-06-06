package technology.sola.engine.input;

import technology.sola.math.linear.Vector2D;

import java.util.EnumMap;
import java.util.Map;

public class MouseInput {
  private final Map<MouseButton, Boolean> mouseDownMap = new EnumMap<>(MouseButton.class);
  private final Map<MouseButton, ButtonState> mouseStatusMap = new EnumMap<>(MouseButton.class);
  private Vector2D lastEventPosition = new Vector2D(0, 0);
  private Vector2D currentMousePosition = null;

  public boolean isMouseClicked(MouseButton buttonName) {
    return mouseStatusMap.get(buttonName) == ButtonState.CLICKED;
  }

  public boolean isMouseDragged(MouseButton buttonName) {
    return mouseStatusMap.get(buttonName) == ButtonState.DRAGGED;
  }

  public Vector2D getMousePosition() {
    return currentMousePosition;
  }

  public void updateStatusOfMouse() {
    currentMousePosition = lastEventPosition;

    mouseDownMap
      .forEach((buttonName, isDown) -> {
        if (isDown) {
          if (mouseStatusMap.get(buttonName) == ButtonState.CLICKED) {
            mouseStatusMap.put(buttonName, ButtonState.DRAGGED);
          } else if (mouseStatusMap.get(buttonName) == ButtonState.RELEASED) {
            mouseStatusMap.put(buttonName, ButtonState.CLICKED);
          }
        } else {
          mouseStatusMap.put(buttonName, ButtonState.RELEASED);
        }
      });
  }

  public void onMouseMoved(MouseEvent mouseEvent) {
    updateMousePosition(mouseEvent);
  }

  public void onMousePressed(MouseEvent mouseEvent) {
    updateMousePosition(mouseEvent);
    mouseDownMap.put(mouseEvent.getButton(), true);
  }

  public void onMouseReleased(MouseEvent mouseEvent) {
    updateMousePosition(mouseEvent);
    mouseDownMap.put(mouseEvent.getButton(), false);
  }

  private void updateMousePosition(MouseEvent mouseEvent) {
    lastEventPosition = new Vector2D((float)mouseEvent.getX(), (float)mouseEvent.getY());
  }

  private enum ButtonState {
    DRAGGED,
    CLICKED,
    RELEASED
  }
}
