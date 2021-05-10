package technology.sola.engine.event;

import java.util.*;

public class EventHub {
  private final Map<Class<? extends Event>, List<EventListener>> eventListenersMap;

  public EventHub() {
    eventListenersMap = new HashMap<>();
  }

  public void add(EventListener<?> eventListener) {
    eventListenersMap.computeIfAbsent(eventListener.getEventClass(), key -> new LinkedList<>()).add(eventListener);
  }

  public void remove(EventListener<?> eventListener) {
    eventListenersMap.computeIfPresent(eventListener.getEventClass(), (key, value) -> {
      value.remove(eventListener);
      return value;
    });
  }

  public void emit(Event<?> event) {
    eventListenersMap.computeIfPresent(event.getClass(), (key, value) -> {
      value.forEach(eventListener -> eventListener.onEvent(event));
      return value;
    });
  }
}
