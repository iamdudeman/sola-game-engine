package technology.sola.engine.platform.server;

import technology.sola.engine.networking.socket.SocketClient;
import technology.sola.engine.networking.socket.SocketMessage;

public class SocketServer {
  public void start(int port) {

  }

  public void stop() {

  }

  public void messageClient(SocketClient socketClient, SocketMessage<?> message) {

  }

  public void broadcastMessage(SocketMessage<?> message, SocketClient... ignoreClients) {

  }

  // onConnect(client)
  // onDisconnect(client)
  // onMessage(client, message)
}
