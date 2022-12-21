package technology.sola.engine.networking.socket;

public interface SocketClient {
  void sendMessage(SocketMessage socketMessage);

  void connect(String host, int port);

  void disconnect();

  boolean isConnected();
}
