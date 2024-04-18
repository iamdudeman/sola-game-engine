package technology.sola.engine.defaults.input;

import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

public record KeyControlInput(Key key, State state) implements ControlInput<KeyControlInput.State> {
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
