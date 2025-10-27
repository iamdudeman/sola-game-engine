package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

/**
 * Contains the details about the touch interaction.
 *
 * @param x     the x coordinate of the touch
 * @param y     the y coordinate of the touch
 * @param phase the {@link TouchPhase} of the touch
 * @param id    the unique id of the touch
 */
@NullMarked
public record Touch(
  float x,
  float y,
  TouchPhase phase,
  int id
) {
}
