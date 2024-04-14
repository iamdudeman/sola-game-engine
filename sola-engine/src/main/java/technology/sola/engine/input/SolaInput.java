package technology.sola.engine.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
todo
this could be populated by a controls.input.json file or something like that
[
  {
    "id": "jump",
    "inputs": [
      [{ "type": "key", "key": "A", "state": "PRESSED" }, { "type": "key", "key": "SHIFT", "state": "PRESSED" }]
    ]
  },
  {
    "id": "jump",
    "inputs": [
      { "type": "mouse", "button": "PRIMARY", "state": "PRESSED" }
    ]
  }
]
 */

public class SolaInput {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolaInput.class);
  private final Map<String, Control> controlMap = new HashMap<>();
  private final KeyboardInput keyboardInput;
  private final MouseInput mouseInput;

  public SolaInput(KeyboardInput keyboardInput, MouseInput mouseInput) {
    this.keyboardInput = keyboardInput;
    this.mouseInput = mouseInput;
  }

  public boolean isActive(String controlId) {
    var control = controlMap.get(controlId);

    if (control == null) {
      LOGGER.warn("Control with id {} not found", controlId);
      return false;
    }

    for (var inputs : control.inputs) {
      boolean isInputActive = true;

      for (var input : inputs) {
        if (!input.isActive(keyboardInput, mouseInput)) {
          isInputActive = false;
          break;
        }
      }

      if (isInputActive) {
        return true;
      }
    }

    return false;
  }

  public void setControl(String id, Control control) {
    controlMap.put(id, control);
  }

  public record Control(String id, List<List<Input<?>>> inputs) {
  }

  interface Input<T> {
    T state();

    boolean isActive(KeyboardInput keyboardInput, MouseInput mouseInput);
  }

  public record KeyInput(Key key, KeyState state) implements Input<KeyState> {
    @Override
    public boolean isActive(KeyboardInput keyboardInput, MouseInput mouseInput) {
      if (state == KeyState.PRESSED) {
        return keyboardInput.isKeyPressed(key);
      } else {
        return keyboardInput.isKeyHeld(key);
      }
    }
  }

  public enum KeyState {
    PRESSED,
    HELD
  }

  public record MouseButtonInput(MouseButton button, MouseState state) implements Input<MouseState> {
    @Override
    public boolean isActive(KeyboardInput keyboardInput, MouseInput mouseInput) {
      if (state == MouseState.PRESSED) {
        return mouseInput.isMousePressed(button);
      } else {
        return mouseInput.isMouseDragged(button);
      }
    }
  }

  public enum MouseState {
    PRESSED,
    DRAGGED
  }
}
