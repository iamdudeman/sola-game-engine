package technology.sola.engine.graphics.sprite.exception;

public class SpriteNotFoundException extends RuntimeException {
  public SpriteNotFoundException(String spriteId) {
    super("Sprite not found with id [" + spriteId + "]");
  }
}
