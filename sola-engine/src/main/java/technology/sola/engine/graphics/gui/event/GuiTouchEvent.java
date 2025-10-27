package technology.sola.engine.graphics.gui.event;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.input.TouchEvent;

/**
 * GuiKeyEvent is a {@link GuiEvent} implementation for {@link GuiTouchEvent}s.
 */
@NullMarked
public class GuiTouchEvent extends GuiEvent {
  private final TouchEvent touchEvent;

  /**
   * Creates an instance of the event.
   *
   * @param touchEvent the original {@link TouchEvent}
   */
  public GuiTouchEvent(TouchEvent touchEvent) {
    this.touchEvent = touchEvent;
  }

  /**
   * @return the original {@link TouchEvent}
   */
  public TouchEvent getTouchEvent() {
    return touchEvent;
  }
}
