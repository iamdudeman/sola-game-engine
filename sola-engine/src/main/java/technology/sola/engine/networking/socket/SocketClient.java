package technology.sola.engine.networking.socket;

import java.io.IOException;

public interface SocketClient {
  int id();

  void sendMessage(SocketMessage<?> socketMessage) throws IOException;

  void connect(String host, int port);

  void disconnect();

  boolean isConnected();
}
