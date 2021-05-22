package technology.sola.engine.event;

public interface EventListener<T extends Event<?>> {
  Class<T> getEventClass();

  void onEvent(T event);
}
