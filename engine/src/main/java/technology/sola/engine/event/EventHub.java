package technology.sola.engine.event;

import java.util.*;

public class EventHub {
  private final Map<Class<?>, List<EventListener>> messageListenersMap;

  public EventHub() {
    messageListenersMap = new HashMap<>();
  }

  public void subscribe(EventListener<?> eventListener) {
    messageListenersMap.computeIfAbsent(eventListener.getMessageClass(), key -> new LinkedList<>()).add(eventListener);
  }

  public void unsubscribe(EventListener<?> eventListener) {
    messageListenersMap.computeIfPresent(eventListener.getMessageClass(), (key, value) -> {
      value.remove(eventListener);
      return value;
    });
  }

  public void emit(Event<?> event) {
    messageListenersMap.get(event.getMessageClass()).forEach(eventListener -> eventListener.onMessage(event));
  }
}
