package technology.sola.engine.input;

import technology.sola.math.linear.Vector2D;

public record Touch(
  Vector2D position,
  TouchPhase phase
) {
}
