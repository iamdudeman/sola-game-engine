package technology.sola.engine.assets.input;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

/**
 * KeyControlInputCondition is a {@link ControlInputCondition} to check if a {@link Key} is in an active state.
 *
 * @param key   the {@link Key} to check
 * @param state the {@link State} needed to be considered active
 */
@NullMarked
public record KeyControlInputCondition(
  Key key,
  State state
) implements ControlInputCondition<KeyControlInputCondition.State> {
  @Override
  public boolean isActive(KeyboardInput keyboardInput, MouseInput mouseInput) {
    if (state == State.PRESSED) {
      return keyboardInput.isKeyPressed(key);
    } else {
      return keyboardInput.isKeyHeld(key);
    }
  }

  /**
   * The possible states for {@link KeyControlInputCondition}.
   */
  public enum State {
    /**
     * The condition is considered active if the {@link Key} is held.
     */
    HELD,
    /**
     * The condition is considered active if the {@link Key} is pressed.
     */
    PRESSED,
  }
}
