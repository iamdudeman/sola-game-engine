package technology.sola.engine.defaults.controls;

import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.input.MouseInput;

public record MouseButtonControlInputCondition(
  MouseButton button,
  State state
) implements ControlInputCondition<MouseButtonControlInputCondition.State> {
  @Override
  public boolean isActive(KeyboardInput keyboardInput, MouseInput mouseInput) {
    if (state == State.PRESSED) {
      return mouseInput.isMousePressed(button);
    } else {
      return mouseInput.isMouseDragged(button);
    }
  }

  public enum State {
    DRAGGED,
    PRESSED,
  }
}
