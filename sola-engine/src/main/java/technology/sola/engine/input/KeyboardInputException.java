package technology.sola.engine.input;

import java.io.Serial;

public class KeyboardInputException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -5667198620006914560L;

  KeyboardInputException(int keyCode) {
    super(String.format("Key with code [%d] cannot be handled yet", keyCode));
  }
}
