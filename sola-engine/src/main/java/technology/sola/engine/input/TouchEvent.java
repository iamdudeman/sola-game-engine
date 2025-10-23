package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record TouchEvent(
  Touch touch
) {
}
