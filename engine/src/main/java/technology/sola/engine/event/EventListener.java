package technology.sola.engine.event;

public interface EventListener<T> {
  Class<T> getMessageClass();

  void onMessage(Event<T> event);
}
