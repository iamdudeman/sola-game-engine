package technology.sola.engine.server;

import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketMessage;

import java.io.Closeable;

public interface ClientConnection extends Runnable, Closeable {
  long getClientId();

  NetworkQueue<SocketMessage> getNetworkQueue();

  void sendMessage(SocketMessage socketMessage);

  @FunctionalInterface
  interface OnMessageHandler {
    boolean accept(ClientConnection clientConnection, SocketMessage socketMessage);
  }
}
