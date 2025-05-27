package technology.sola.engine.graphics.gui.event;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.input.MouseEvent;

/**
 * GuiKeyEvent is a {@link GuiEvent} implementation for {@link GuiMouseEvent}s.
 */
@NullMarked
public class GuiMouseEvent extends GuiEvent {
  private final MouseEvent mouseEvent;

  /**
   * Creates an instance of the event.
   *
   * @param mouseEvent the original {@link MouseEvent}
   */
  public GuiMouseEvent(MouseEvent mouseEvent) {
    this.mouseEvent = mouseEvent;
  }

  /**
   * @return the original {@link MouseEvent}
   */
  public MouseEvent getMouseEvent() {
    return mouseEvent;
  }
}
