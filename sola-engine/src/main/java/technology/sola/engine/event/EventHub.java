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

  public <T extends Event> void add(Class<T> eventClass, EventListener<T> eventListener) {
    var eventListeners = eventListenersMap.computeIfAbsent(eventClass, key -> new ArrayList<>());

    eventListeners.add(eventListener);
  }

  public <T extends Event> void remove(Class<T> eventClass, EventListener<T> eventListener) {
    var eventListeners = eventListenersMap.get(eventClass);

    if (eventListeners != null) {
      eventListeners.remove(eventListener);
    }
  }

  public <T extends Event> void off(Class<T> eventClass) {
    eventListenersMap.put(eventClass, new ArrayList<>());
  }

  @SuppressWarnings("unchecked")
  public <T extends Event> void emit(T event) {
    var eventListeners = eventListenersMap.get(event.getClass());

    if (eventListeners != null) {
      for (var eventListener : eventListeners) {
        eventListener.onEvent(event);
      }
    }
  }
}
