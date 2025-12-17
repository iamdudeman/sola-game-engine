package technology.sola.engine.core.event;

/**
 * Subscription holds a callback to remove an event listener from {@link technology.sola.engine.core.SolaPlatform}.
 */
@FunctionalInterface
public interface Subscription {
  /**
   * Unsubscribe callback that does nothing for events that are not supported by
   * the {@link technology.sola.engine.core.SolaPlatform}.
   */
  Subscription NOT_SUPPORTED = () -> {};

  /**
   * Call to remove the event listener from the {@link technology.sola.engine.core.SolaPlatform}.
   */
  void unsubscribe();
}
