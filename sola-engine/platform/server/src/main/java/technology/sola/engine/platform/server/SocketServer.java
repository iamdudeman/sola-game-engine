package technology.sola.engine.platform.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.networking.socket.SocketClient;
import technology.sola.engine.networking.socket.SocketMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class SocketServer {
  private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);
  private final Map<Long, ClientConnection> clientConnectionMap = new HashMap<>();
  private long clientCount = 0;
  private ServerSocket serverSocket;
  private boolean isAcceptingConnections = true;
  private boolean isStarted = false;

  public void start(int port) {
    isStarted = true;

    new Thread(() -> {
      try {
        serverSocket = new ServerSocket(port);

        do {
          LOGGER.info("Waiting for connection...");
          ClientConnection clientConnection = new ClientConnection(serverSocket.accept(), nextClientId());
          LOGGER.info("Client {} connected", clientConnection.getClientId());
          clientConnectionMap.put(clientConnection.getClientId(), clientConnection);
          new Thread(clientConnection).start();
        } while (isAcceptingConnections);
      } catch (IOException ex) {
        LOGGER.error(ex.getMessage(), ex);
      }
    }).start();
  }

  public void stop() {
    if (!isStarted) {
      return;
    }

    try {
      LOGGER.info("Stopping server");
      isAcceptingConnections = false;
      if (serverSocket != null) {
        serverSocket.close();
      }

      clientConnectionMap.values().forEach(ClientConnection::close);
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }

  public void message(long id, SocketMessage<?> socketMessage) {
    try {
      ClientConnection clientConnection = clientConnectionMap.get(id);

      if (clientConnection == null) {
        LOGGER.warn("ClientConnection with id {} does not exist", id);
      } else {
        clientConnection.sendMessage(socketMessage);
      }
    } catch (IOException ex) {
      LOGGER.error("Failed to send message to {}", id, ex);
    }
  }

  public void broadcast(SocketMessage<?> socketMessage, SocketClient... ignoreClients) {
    clientConnectionMap.forEach((id, client) -> {
      try {
        client.sendMessage(socketMessage);
      } catch (IOException ex) {
        LOGGER.error("Failed to send broadcast to {}", id, ex);
      }
    });
  }

  // onConnect(client)
  // onDisconnect(client)
  // onMessage(client, message)

  private long nextClientId() {
    return clientCount++;
  }
}
