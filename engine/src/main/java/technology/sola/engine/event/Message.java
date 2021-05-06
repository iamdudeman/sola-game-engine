package technology.sola.engine.event;

public interface Message<T> {
  T getBody();
}
