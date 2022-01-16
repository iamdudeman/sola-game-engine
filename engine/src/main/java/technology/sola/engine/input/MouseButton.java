package technology.sola.engine.input;

import java.util.Arrays;

public enum MouseButton {
  NONE(0),
  PRIMARY(1),
  MIDDLE(2),
  SECONDARY(3),
  BACK(4),
  FORWARD(5);

  public static MouseButton valueOf(int buttonCode) {
    if (buttonCode < 0 || buttonCode > MouseButton.values().length) {
      throw new IllegalArgumentException(buttonCode + " must be in MouseButton values " + Arrays.toString(MouseButton.values()));
    }

    return MouseButton.values()[buttonCode];
  }

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
