package technology.sola.engine.input;

public record MouseEvent(MouseButton button, int x, int y) {
  public MouseEvent(int buttonCode, int x, int y) {
    this(MouseButton.valueOf(buttonCode), x, y);
  }
}
