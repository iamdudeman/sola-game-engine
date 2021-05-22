package technology.sola.engine.input;

public class KeyEvent {
  private final Key key;

  public KeyEvent(Key key) {
    this.key = key;
  }

  public Key getKey() {
    return key;
  }
}
