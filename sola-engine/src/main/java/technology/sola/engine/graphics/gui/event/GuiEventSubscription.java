package technology.sola.engine.graphics.gui.event;

import org.jspecify.annotations.NullMarked;

/**
 * GuiEventSubscription is the handle to the event subscriber when it is registered to a {@link GuiEventListenerList}.
 */
@NullMarked
@FunctionalInterface
public interface GuiEventSubscription {
  /**
   * Removes the {@link GuiEventListener} from the {@link GuiEventListenerList} when called.
   */
  void unsubscribe();
}
