package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

import java.io.Serial;

/**
 * KeyboardInputException is an exception thrown when a key event is triggered with an unrecognized key code.
 */
@NullMarked
public class KeyboardInputException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -5667198620006914560L;

  KeyboardInputException(int keyCode) {
    super(String.format("Key with code [%d] cannot be handled yet", keyCode));
  }
}
