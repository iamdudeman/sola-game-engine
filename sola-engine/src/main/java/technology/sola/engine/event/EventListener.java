package technology.sola.engine.event;

@FunctionalInterface
public interface EventListener<T extends Event<?>> {
  void onEvent(T event);
}
