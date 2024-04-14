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
  private final Map<String, List<List<Input<?>>>> controlMap = new HashMap<>();
  private final KeyboardInput keyboardInput;
  private final MouseInput mouseInput;

  public SolaInput(KeyboardInput keyboardInput, MouseInput mouseInput) {
    this.keyboardInput = keyboardInput;
    this.mouseInput = mouseInput;
  }

  public boolean isActive(String controlId) {
    var inputGroups = controlMap.get(controlId);

    if (inputGroups == null) {
      LOGGER.warn("Control with id {} not found", controlId);
      return false;
    }

    for (var inputs : inputGroups) {
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

  // todo ability to update one control
  // todo convenience method to not need list of list

  public SolaInput setControl(String id, List<List<Input<?>>> inputs) {
    controlMap.put(id, inputs);

    return this;
  }

  public interface Input<T> {
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
