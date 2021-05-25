package technology.sola.engine.input;

public class KeyEvent {
  private final int keyCode;

  public KeyEvent(int keyCode) {
    this.keyCode = keyCode;
  }

  public int getKeyCode() {
    return keyCode;
  }
}
