package technology.sola.engine.graphics.guiv2.event;

/**
 * GuiEventSubscription is the handle to the event subscriber when it is registered to a {@link GuiEventListenerList}.
 */
@FunctionalInterface
public interface GuiEventSubscription {
  /**
   * Removes the {@link GuiEventListener} from the {@link GuiEventListenerList} when called.
   */
  void unsubscribe();
}
