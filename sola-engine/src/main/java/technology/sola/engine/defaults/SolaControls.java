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

/**
 * SolaControls contains control definitions for a {@link technology.sola.engine.core.Sola} to use. Each control that is
 * added can have multiple {@link ControlInput}s. If any {@link ControlInput} is in an active state then the control
 * will be considered active.
 */
public class SolaControls {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolaControls.class);
  private final Map<String, List<ControlInput>> controls = new HashMap<>();
  private final KeyboardInput keyboardInput;
  private final MouseInput mouseInput;

  /**
   * Creates a SolaControls instance wrapping all supported input devices.
   *
   * @param keyboardInput the {@link KeyboardInput}
   * @param mouseInput    the {@link MouseInput}
   */
  public SolaControls(KeyboardInput keyboardInput, MouseInput mouseInput) {
    this.keyboardInput = keyboardInput;
    this.mouseInput = mouseInput;
  }

  /**
   * Checks to see if specified control is currently active.
   *
   * @param controlId the control to check
   * @return true if the control is active
   */
  public boolean isActive(String controlId) {
    var inputGroups = controls.get(controlId);

    if (inputGroups == null) {
      LOGGER.warn("Control with id {} not found", controlId);
      return false;
    }

    for (var inputs : inputGroups) {
      boolean isInputActive = true;

      for (var condition : inputs.conditions()) {
        if (!condition.isActive(keyboardInput, mouseInput)) {
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

  /**
   * Adds a control with only a single specified {@link ControlInput}.
   *
   * @param id           the control id
   * @param controlInput the {@link ControlInput} for the control
   * @return this
   */
  public SolaControls addControl(String id, ControlInput controlInput) {
    controls.put(id, List.of(controlInput));

    return this;
  }

  /**
   * Adds a control with specified {@link ControlInput}s.
   *
   * @param id            the control id
   * @param controlInputs the {@link ControlInput}s for the control
   * @return this
   */
  public SolaControls addControl(String id, List<ControlInput> controlInputs) {
    controls.put(id, controlInputs);

    return this;
  }

  /**
   * Adds all controls from a {@link ControlsConfig}.
   *
   * @param controlsConfig the {@link ControlsConfig}
   * @return this
   */
  public SolaControls addControls(ControlsConfig controlsConfig) {
    controls.clear();
    controlsConfig.controls().forEach(this::addControl);

    return this;
  }
}
