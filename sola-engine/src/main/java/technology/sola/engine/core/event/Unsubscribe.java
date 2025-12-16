package technology.sola.engine.core.event;

/**
 * Unsubscribe is a callback to remove an event listener from {@link technology.sola.engine.core.SolaPlatform}.
 */
@FunctionalInterface
public interface Unsubscribe {
  /**
   * Unsubscribe callback that does nothing for events that are not supported by
   * the {@link technology.sola.engine.core.SolaPlatform}.
   */
  Unsubscribe NOT_SUPPORTED = () -> {};

  /**
   * Call to remove the event listener from the {@link technology.sola.engine.core.SolaPlatform}.
   */
  void unsubscribe();
}
