package technology.sola.engine.event;

public interface MessageListener<T> {
  void onMessage(Message<T> message);
}
