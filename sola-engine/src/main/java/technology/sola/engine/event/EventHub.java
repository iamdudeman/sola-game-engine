package technology.sola.engine.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHub {
  @SuppressWarnings("rawtypes")
  private final Map<Class<? extends Event>, List<EventListener>> eventListenersMap;

  public EventHub() {
    eventListenersMap = new HashMap<>();
  }

  public <T extends Event<?>> void add(EventListener<T> eventListener, Class<T> eventClass) {
    eventListenersMap.computeIfAbsent(eventClass, key -> new ArrayList<>()).add(eventListener);
  }

  public <T extends Event<?>> void remove(EventListener<T> eventListener, Class<T> eventClass) {
    eventListenersMap.computeIfPresent(eventClass, (key, value) -> {
      value.remove(eventListener);
      return value;
    });
  }

  public <T extends Event<?>> void off(Class<T> eventClass) {
    eventListenersMap.put(eventClass, new ArrayList<>());
  }

  @SuppressWarnings("unchecked")
  public <T extends Event<?>> void emit(T event) {
    eventListenersMap.computeIfPresent(event.getClass(), (key, value) -> {
      value.forEach(eventListener -> eventListener.onEvent(event));
      return value;
    });
  }
}
