package technology.sola.engine.defaults.controls;

import java.util.List;

public record ControlInput(
  List<ControlInputCondition<?>> conditions
) {
}
