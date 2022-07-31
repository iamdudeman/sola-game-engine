package technology.sola.engine.graphics.exception;

import java.io.Serial;

public class SpriteNotFoundException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -4943059122204597485L;

  public SpriteNotFoundException(String spriteId) {
    super("Sprite not found with id [" + spriteId + "]");
  }
}
