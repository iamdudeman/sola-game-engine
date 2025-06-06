package technology.sola.engine.graphics.gui.event;

import org.jspecify.annotations.NullMarked;

/**
 * GuiEventListener {@link GuiEventListener#onEvent(GuiEvent)} method is called whenever the event of the type is
 * emitted.
 *
 * @param <E> the event type
 */
@NullMarked
public interface GuiEventListener<E extends GuiEvent> {
  /**
   * Callback that is called with the {@link GuiEvent} that was emitted.
   *
   * @param event the event that was emitted
   */
  void onEvent(E event);
}
