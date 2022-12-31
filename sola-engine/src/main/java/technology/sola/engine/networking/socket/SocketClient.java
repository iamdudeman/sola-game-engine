package technology.sola.engine.networking.socket;

import technology.sola.engine.networking.NetworkQueue;

public interface SocketClient {
  NetworkQueue<SocketMessage> getNetworkQueue();

  void sendMessage(SocketMessage socketMessage);

  void connect(String host, int port);

  void disconnect();

  boolean isConnected();
}
