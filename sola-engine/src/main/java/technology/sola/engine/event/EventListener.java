package technology.sola.engine.event;

import org.jspecify.annotations.NullMarked;

/**
 * The EventListener interface defines the api for listening to emitted {@link Event}s of a certain type.
 *
 * @param <T> the type of the event
 */
@NullMarked
@FunctionalInterface
public interface EventListener<T extends Event> {
  /**
   * Function called whenever an {@link Event} that is listened to is emitted.
   *
   * @param event the event the was emitted
   */
  void onEvent(T event);
}
