package technology.sola.engine.networking.socket;

public interface SocketMessage<T> {
  SocketHeader header();

  T body();
}
