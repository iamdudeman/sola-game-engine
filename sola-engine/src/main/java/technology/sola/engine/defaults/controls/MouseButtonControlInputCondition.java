package technology.sola.engine.defaults.controls;

import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.input.MouseInput;

/**
 * MouseButtonInputCondition is a {@link ControlInputCondition} to check if a {@link MouseButton} is in an active state.
 *
 * @param button the {@link MouseButton} to check
 * @param state  the {@link State} needed to be considered active
 */
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

  /**
   * The possible states for {@link MouseButtonControlInputCondition}.
   */
  public enum State {
    /**
     * The condition is considered active if the {@link MouseButton} is held and being dragged.
     */
    DRAGGED,
    /**
     * The condition is considered active if the {@link MouseButton} is pressed.
     */
    PRESSED,
  }
}
