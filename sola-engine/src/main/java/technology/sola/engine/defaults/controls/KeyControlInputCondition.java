package technology.sola.engine.defaults.controls;

import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

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

  public enum State {
    HELD,
    PRESSED,
  }
}
