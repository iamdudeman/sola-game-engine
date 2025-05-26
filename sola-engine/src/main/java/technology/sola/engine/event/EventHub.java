package technology.sola.engine.event;

import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EventHub handles distribution of {@link Event}s that are emitted to their respective {@link EventListener}s that have
 * been added.
 */
@NullMarked
public class EventHub {
  @SuppressWarnings("rawtypes")
  private final Map<Class<? extends Event>, List<EventListener>> eventListenersMap = new HashMap<>();

  /**
   * Adds an {@link EventListener} that listens to events of desired type.
   *
   * @param eventClass    the class of the type of events to listen to
   * @param eventListener the {@code EventListener}
   * @param <T>           the type of the event
   */
  public <T extends Event> void add(Class<T> eventClass, EventListener<T> eventListener) {
    var eventListeners = eventListenersMap.computeIfAbsent(eventClass, key -> new ArrayList<>());
    var newListeners = new ArrayList<>(eventListeners);

    newListeners.add(eventListener);
    eventListenersMap.put(eventClass, newListeners);
  }

  /**
   * Removes an {@link EventListener} that is listening to events of desired type.
   *
   * @param eventClass    the class of the type of events to remove a listener from
   * @param eventListener the {@code EventListener}
   * @param <T>           the type of the event
   */
  public <T extends Event> void remove(Class<T> eventClass, EventListener<T> eventListener) {
    var eventListeners = eventListenersMap.get(eventClass);

    if (eventListeners != null) {
      var newListeners = new ArrayList<>(eventListeners);
      newListeners.remove(eventListener);
      eventListenersMap.put(eventClass, newListeners);
    }
  }

  /**
   * Removes all {@link EventListener} for an event type.
   *
   * @param eventClass the class of the type of events to remove a listener from
   * @param <T>        the type of the event
   */
  public <T extends Event> void off(Class<T> eventClass) {
    eventListenersMap.put(eventClass, new ArrayList<>());
  }

  /**
   * Sends an {@link Event} to all {@link EventListener}s that have been added for that event type.
   *
   * @param event the {@code Event} to send
   * @param <T>   the type of the event
   */
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
