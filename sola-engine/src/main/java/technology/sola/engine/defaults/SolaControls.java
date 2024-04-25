package technology.sola.engine.defaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.assets.input.ControlsConfig;
import technology.sola.engine.defaults.controls.ControlInput;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolaControls {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolaControls.class);
  private final Map<String, List<List<ControlInput<?>>>> controls = new HashMap<>();
  private final KeyboardInput keyboardInput;
  private final MouseInput mouseInput;

  public SolaControls(KeyboardInput keyboardInput, MouseInput mouseInput) {
    this.keyboardInput = keyboardInput;
    this.mouseInput = mouseInput;
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

  public SolaControls addControl(String id, List<List<ControlInput<?>>> inputs) {
    controls.put(id, inputs);

    return this;
  }

  public SolaControls setControls(ControlsConfig controlsConfig) {
    controls.clear();
    controlsConfig.controls().forEach(this::addControl);

    return this;
  }
}
