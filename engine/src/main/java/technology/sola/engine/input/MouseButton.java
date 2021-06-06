package technology.sola.engine.input;

public enum MouseButton {
  NONE(0),
  PRIMARY(1),
  MIDDLE(2),
  SECONDARY(3),
  BACK(4),
  FORWARD(5);

  private final int code;

  MouseButton(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  @Override
  public String toString() {
    return code + ": " + name();
  }
}
