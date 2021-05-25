package technology.sola.engine.event;

public interface EventListener<T extends Event<?>> {
  void onEvent(T event);
}
