package technology.sola.engine.defaults.controls;

import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

public interface ControlInputCondition<T> {
  T state();

  boolean isActive(KeyboardInput keyboardInput, MouseInput mouseInput);
}
