package technology.sola.engine.assets.input;

import technology.sola.engine.assets.Asset;
import technology.sola.engine.defaults.input.ControlInput;

import java.util.List;
import java.util.Map;

public record InputConfig(Map<String, List<List<ControlInput<?>>>> controlMap) implements Asset {
}
