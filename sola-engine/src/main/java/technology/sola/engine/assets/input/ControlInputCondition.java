package technology.sola.engine.assets.input;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

/**
 * ControlInputCondition indicates whether a condition for a {@link ControlInput} is active or not.
 *
 * @param <T> the type for the state of the input condition (ie pressed, held)
 */
@NullMarked
public interface ControlInputCondition<T> {
  /**
   * The state the input needs to be in to be considered active.
   *
   * @return the state needed to be considered active
   */
  T state();

  /**
   * Checks the state of the input to see if the condition is active or not.
   *
   * @param keyboardInput the {@link KeyboardInput}
   * @param mouseInput    the {@link MouseInput}
   * @return true if the condition is active
   */
  boolean isActive(KeyboardInput keyboardInput, MouseInput mouseInput);
}
