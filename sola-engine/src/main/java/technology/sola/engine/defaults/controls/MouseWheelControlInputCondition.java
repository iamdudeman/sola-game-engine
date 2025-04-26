package technology.sola.engine.defaults.controls;

import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

/**
 * MouseWheelControlInputCondition is a {@link ControlInputCondition} to check if the mouse wheel is in an active state.
 *
 * @param state the {@link MouseWheelControlInputCondition.State} needed to be considered active
 */
public record MouseWheelControlInputCondition(
  State state
) implements ControlInputCondition<MouseWheelControlInputCondition.State> {
  @Override
  public boolean isActive(KeyboardInput keyboardInput, MouseInput mouseInput) {
    var mouseWheel = mouseInput.getMouseWheel();

    return switch (state) {
      case UP -> mouseWheel.isUp();
      case DOWN -> mouseWheel.isDown();
      case LEFT -> mouseWheel.isLeft();
      case RIGHT -> mouseWheel.isRight();
    };
  }

  /**
   * The possible states for {@link MouseWheelControlInputCondition}.
   */
  public enum State {
    /**
     * Mouse wheel was moved up.
     */
    UP,
    /**
     * Mouse wheel was moved down.
     */
    DOWN,
    /**
     * Mouse wheel was moved left.
     */
    LEFT,
    /**
     * Mouse wheel was moved right.
     */
    RIGHT,
  }
}
