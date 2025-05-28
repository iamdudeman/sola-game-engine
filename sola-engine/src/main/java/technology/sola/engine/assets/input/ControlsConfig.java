package technology.sola.engine.assets.input;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.Asset;

import java.util.List;
import java.util.Map;

/**
 * ControlsConfig is an {@link Asset} for control configurations. Each control can have multiple
 * {@link ControlInput}s. If any {@link ControlInput} is in an active state then the control will be considered active.
 *
 * @param controls the Map of control ids to controls
 */
@NullMarked
public record ControlsConfig(
  Map<String, List<ControlInput>> controls
) implements Asset {
}
