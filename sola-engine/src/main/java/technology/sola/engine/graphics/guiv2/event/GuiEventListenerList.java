package technology.sola.engine.graphics.guiv2.event;

import java.util.ArrayList;
import java.util.List;

public class GuiEventListenerList<E extends GuiEvent> {
  private final List<GuiEventListener<E>> eventListeners = new ArrayList<>();

  public GuiEventSubscription on(GuiEventListener<E> eventListener) {
    eventListeners.add(eventListener);

    return () -> eventListeners.remove(eventListener);
  }

  public GuiEventSubscription once(GuiEventListener<E> eventListener) {
    GuiEventListener<E> oneTimeEventListener = new OneTimeEventListener(eventListener);

    return () -> eventListeners.remove(oneTimeEventListener);
  }

  public void off() {
    eventListeners.clear();
  }

  public void emit(E event) {
    eventListeners.forEach(eventListener -> eventListener.onEvent(event));
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
