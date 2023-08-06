package technology.sola.engine.graphics.gui.event;

import technology.sola.engine.input.MouseButton;
import technology.sola.engine.input.MouseEvent;

public class GuiMouseEvent extends GuiEvent {
  private final MouseEvent mouseEvent;
  private final Type type;

  public GuiMouseEvent(MouseEvent mouseEvent, Type type) {
    this.mouseEvent = mouseEvent;
    this.type = type;
  }

  public int x() {
    return mouseEvent.x();
  }

  public int y() {
    return mouseEvent.y();
  }

  public MouseButton button() {
    return mouseEvent.button();
  }

  public Type getType() {
    return type;
  }

  public enum Type {
    PRESS,
    RELEASE,
    MOVE
  }
}
