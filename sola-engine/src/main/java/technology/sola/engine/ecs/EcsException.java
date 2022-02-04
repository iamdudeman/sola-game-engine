package technology.sola.engine.ecs;

import java.io.Serial;

class EcsException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -5303251140826169872L;

  EcsException(String message) {
    super(message);
  }
}
