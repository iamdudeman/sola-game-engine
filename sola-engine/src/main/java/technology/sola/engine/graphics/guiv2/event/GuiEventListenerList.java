package technology.sola.engine.graphics.guiv2.event;

import java.util.ArrayList;
import java.util.List;

/**
 * GuiEventListenerList is a data structure that holds {@link GuiEventListener}s for a particular kind of {@link GuiEvent}.
 *
 * @param <E> the GuiEvent type
 */
public class GuiEventListenerList<E extends GuiEvent> {
  private final List<GuiEventListener<E>> eventListeners = new ArrayList<>();

  /**
   * Add a {@link GuiEventListener} that fires every time a {@link GuiEvent} is {@link GuiEventListenerList#emit(GuiEvent)}.
   *
   * @param eventListener the listener to add
   * @return a {@link GuiEventSubscription} handle
   */
  public GuiEventSubscription on(GuiEventListener<E> eventListener) {
    eventListeners.add(eventListener);

    return () -> eventListeners.remove(eventListener);
  }

  /**
   * Add a {@link GuiEventListener} that fires one time a {@link GuiEvent} is {@link GuiEventListenerList#emit(GuiEvent)}.
   *
   * @param eventListener the listener to add
   * @return a {@link GuiEventSubscription} handle
   */
  public GuiEventSubscription once(GuiEventListener<E> eventListener) {
    GuiEventListener<E> oneTimeEventListener = new OneTimeEventListener(eventListener);

    return () -> eventListeners.remove(oneTimeEventListener);
  }

  /**
   * Removes all {@link GuiEventListener}s that have been added.
   */
  public void off() {
    eventListeners.clear();
  }

  /**
   * Emits a {@link GuiEvent} that all registered {@link GuiEventListener}s with respond to.
   *
   * @param event the event emitted
   */
  public void emit(E event) {
    for (var eventListener : eventListeners) {
      eventListener.onEvent(event);
    }
  }

  private class OneTimeEventListener implements GuiEventListener<E> {
    private final GuiEventListener<E> original;

    public OneTimeEventListener(GuiEventListener<E> original) {
      this.original = original;
    }

    @Override
    public void onEvent(E event) {
      original.onEvent(event);
      eventListeners.remove(this);
    }
  }
}
