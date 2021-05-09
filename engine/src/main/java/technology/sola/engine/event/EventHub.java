package technology.sola.engine.event;

import java.util.*;

public class EventHub {
  private final Map<Class<? extends Event>, List<EventListener>> messageListenersMap;

  public EventHub() {
    messageListenersMap = new HashMap<>();
  }

  public void subscribe(EventListener<?> eventListener) {
    messageListenersMap.computeIfAbsent(eventListener.getEventClass(), key -> new LinkedList<>()).add(eventListener);
  }

  public void unsubscribe(EventListener<?> eventListener) {
    messageListenersMap.computeIfPresent(eventListener.getEventClass(), (key, value) -> {
      value.remove(eventListener);
      return value;
    });
  }

  public void emit(Event<?> event) {
    messageListenersMap.computeIfPresent(event.getClass(), (key, value) -> {
      value.forEach(eventListener -> eventListener.onEvent(event));
      return value;
    });
  }
}
