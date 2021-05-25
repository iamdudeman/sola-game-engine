package technology.sola.engine.input;

public class KeyboardInputException extends RuntimeException {
  KeyboardInputException(int keyCode) {
    super(String.format("Key with code [%d] cannot be handled yet", keyCode));
  }
}
