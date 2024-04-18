package technology.sola.engine.defaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.defaults.input.ControlInput;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;
import technology.sola.math.linear.Vector2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolaInput {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolaInput.class);
  private final Map<String, List<List<ControlInput<?>>>> controls = new HashMap<>();
  private final KeyboardInput keyboardInput;
  private final MouseInput mouseInput;

  public SolaInput(KeyboardInput keyboardInput, MouseInput mouseInput) {
    this.keyboardInput = keyboardInput;
    this.mouseInput = mouseInput;
  }

  // todo how does this work when touch input is added
  public Vector2D getMousePosition() {
    return mouseInput.getMousePosition();
  }

  public boolean isActive(String controlId) {
    var inputGroups = controls.get(controlId);

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

  // todo ability to update inputs of one control

  // todo convenience method to not need list of list

  public SolaInput setControl(String id, List<List<ControlInput<?>>> inputs) {
    controls.put(id, inputs);

    return this;
  }
}
