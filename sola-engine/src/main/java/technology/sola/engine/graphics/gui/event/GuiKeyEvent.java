package technology.sola.engine.graphics.gui.event;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.input.KeyEvent;

/**
 * GuiKeyEvent is a {@link GuiEvent} implementation for {@link KeyEvent}s.
 */
@NullMarked
public class GuiKeyEvent extends GuiEvent {
  private final KeyEvent keyEvent;

  /**
   * Creates an instance of the event.
   *
   * @param keyEvent the original {@link KeyEvent}
   */
  public GuiKeyEvent(KeyEvent keyEvent) {
    this.keyEvent = keyEvent;
  }

  /**
   * @return the original {@link KeyEvent}
   */
  public KeyEvent getKeyEvent() {
    return keyEvent;
  }
}
