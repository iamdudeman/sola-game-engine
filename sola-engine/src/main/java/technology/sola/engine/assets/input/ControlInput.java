package technology.sola.engine.assets.input;

import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * A ControlInput is composed of a {@link List} of conditions. All {@link ControlInputCondition}s must be in an active
 * state to be considered active.
 *
 * @param conditions the list of {@link ControlInputCondition}
 */
@NullMarked
public record ControlInput(
  List<ControlInputCondition<?>> conditions
) {
}
