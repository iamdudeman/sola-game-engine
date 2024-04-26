package technology.sola.engine.defaults.controls;

import java.util.List;

/**
 * A ControlInput is composed of a {@link List} of conditions. All {@link ControlInputCondition}s must be in an active
 * state to be considered active.
 *
 * @param conditions the list of {@link ControlInputCondition}
 */
public record ControlInput(
  List<ControlInputCondition<?>> conditions
) {
}
