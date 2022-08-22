package technology.sola.engine.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHub {
  @SuppressWarnings("rawtypes")
  private static final List<EventListener> EMPTY = new ArrayList<>();
  @SuppressWarnings("rawtypes")
  private final Map<Class<? extends Event>, List<EventListener>> eventListenersMap;

  public EventHub() {
    eventListenersMap = new HashMap<>();
  }

  public <T extends Event<?>> void add(EventListener<T> eventListener, Class<T> eventClass) {
    var eventListeners = eventListenersMap.getOrDefault(eventClass, new ArrayList<>());
    var newEventListeners = new ArrayList<>(eventListeners);

    newEventListeners.add(eventListener);

    eventListenersMap.put(eventClass, newEventListeners);
  }

  public <T extends Event<?>> void remove(EventListener<T> eventListener, Class<T> eventClass) {
    var eventListeners = eventListenersMap.get(eventClass);

    if (eventListeners != null) {
      eventListenersMap.put(eventClass, eventListeners.stream().filter(eventListenerPresent -> eventListenerPresent != eventListener).toList());
    }
  }

  public <T extends Event<?>> void off(Class<T> eventClass) {
    eventListenersMap.put(eventClass, new ArrayList<>());
  }

  @SuppressWarnings("unchecked")
  public <T extends Event<?>> void emit(T event) {
    var eventListeners = eventListenersMap.getOrDefault(event.getClass(), EMPTY);

    for (var eventListener : eventListeners) {
      eventListener.onEvent(event);
    }
  }
}
