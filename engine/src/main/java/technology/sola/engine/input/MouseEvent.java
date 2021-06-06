package technology.sola.engine.input;

import java.util.Arrays;

public class MouseEvent {
  private final MouseButton button;
  private final int x;
  private final int y;

  public MouseEvent(int buttonCode, int x, int y) {
    if (buttonCode < 0 || buttonCode > MouseButton.values().length) {
      throw new IllegalArgumentException(buttonCode + " must be in MouseButton values " + Arrays.toString(MouseButton.values()));
    }

    this.button = MouseButton.values()[buttonCode];
    this.x = x;
    this.y = y;
  }

  public MouseEvent(MouseButton button, int x, int y) {
    this.button = button;
    this.x = x;
    this.y = y;
  }

  public MouseButton getButton() {
    return button;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
