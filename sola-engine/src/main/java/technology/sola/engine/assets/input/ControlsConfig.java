package technology.sola.engine.assets.input;

import technology.sola.engine.assets.Asset;
import technology.sola.engine.defaults.controls.ControlInput;

import java.util.List;
import java.util.Map;

public record ControlsConfig(
  Map<String, List<ControlInput>> controls
) implements Asset {
}
