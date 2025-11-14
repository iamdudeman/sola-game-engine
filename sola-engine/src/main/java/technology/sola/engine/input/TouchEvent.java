package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

/**
 * TouchEvent contains data for when a {@link Touch} interaction happens.
 *
 * @param touch the {@link Touch}
 */
@NullMarked
public record TouchEvent(
  Touch touch
) {
}
