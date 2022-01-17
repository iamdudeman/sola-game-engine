package technology.sola.engine.event;

import java.util.*;

public class EventHub {
  private final Map<Class<? extends Event>, List<EventListener>> eventListenersMap;

  public EventHub() {
    eventListenersMap = new HashMap<>();
  }

  public <T extends Event<?>> void add(EventListener<T> eventListener, Class<T> eventClass) {
    eventListenersMap.computeIfAbsent(eventClass, key -> new LinkedList<>()).add(eventListener);
  }

  public <T extends Event<?>> void remove(EventListener<T> eventListener, Class<T> eventClass) {
    eventListenersMap.computeIfPresent(eventClass, (key, value) -> {
      value.remove(eventListener);
      return value;
    });
  }

  public void off(Class<? extends Event<?>> eventClass) {
    eventListenersMap.put(eventClass, new LinkedList<>());
  }

  public void emit(Event<?> event) {
    eventListenersMap.computeIfPresent(event.getClass(), (key, value) -> {
      value.forEach(eventListener -> eventListener.onEvent(event));
      return value;
    });
  }
}
