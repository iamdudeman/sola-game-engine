package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record Touch(
  float x,
  float y,
  TouchPhase phase,
  int id,
  int index
) {
}
