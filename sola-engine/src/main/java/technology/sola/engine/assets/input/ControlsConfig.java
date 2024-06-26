package technology.sola.engine.assets.input;

import technology.sola.engine.assets.Asset;
import technology.sola.engine.defaults.controls.ControlInput;

import java.util.List;
import java.util.Map;

/**
 * ControlsConfig is an {@link Asset} for control configurations. Each control can have multiple
 * {@link ControlInput}s. If any {@link ControlInput} is in an active state then the control will be considered active.
 *
 * @param controls the Map of control ids to controls
 */
public record ControlsConfig(
  Map<String, List<ControlInput>> controls
) implements Asset {
}
