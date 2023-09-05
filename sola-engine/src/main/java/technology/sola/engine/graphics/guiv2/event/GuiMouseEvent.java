package technology.sola.engine.graphics.guiv2.event;

import technology.sola.engine.input.MouseEvent;

public class GuiMouseEvent extends GuiEvent {
  private final MouseEvent mouseEvent;

  public GuiMouseEvent(MouseEvent mouseEvent) {
    this.mouseEvent = mouseEvent;
  }

  public MouseEvent getMouseEvent() {
    return mouseEvent;
  }
}
