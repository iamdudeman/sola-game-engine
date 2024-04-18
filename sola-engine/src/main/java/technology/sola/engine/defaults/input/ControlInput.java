package technology.sola.engine.defaults.input;

import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

public interface ControlInput<T> {
  T state();

  boolean isActive(KeyboardInput keyboardInput, MouseInput mouseInput);
}
