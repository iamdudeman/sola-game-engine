package technology.sola.engine.event;

public interface Event<T> {
  Class<T> getMessageClass();

  T getBody();
}
