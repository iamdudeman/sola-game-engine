package technology.sola.engine.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.ecs.SolaEcs;
import technology.sola.engine.core.GameLoop;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.networking.socket.SocketMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SolaServer {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolaServer.class);
  protected final SolaEcs solaEcs;
  protected final EventHub eventHub;
  private final Map<Long, ClientConnection> clientConnectionMap = new HashMap<>();
  private long clientCount = 0;
  private ServerSocket serverSocket;
  private boolean isAcceptingConnections = true;
  private boolean isStarted = false;
  private final GameLoop gameLoop;

  protected SolaServer(int targetUpdatesPerSecond) {
    solaEcs = new SolaEcs();
    eventHub = new EventHub();
    this.gameLoop = new ServerGameLoop(eventHub, solaEcs::updateWorld, targetUpdatesPerSecond);
  }

  public abstract void initialize();

  public abstract boolean isAllowedConnection(ClientConnection clientConnection);

  public abstract void onConnectionEstablished(ClientConnection clientConnection);

  public abstract void onDisconnect(ClientConnection clientConnection);

  public abstract boolean onMessage(ClientConnection clientConnection, SocketMessage socketMessage);

  public Map<Long, ClientConnection> getClientConnectionMap() {
    return clientConnectionMap;
  }

  public void start(int port) {
    initialize();

    isStarted = true;

    // game loop thread
    new Thread(gameLoop).start();

    // network thread
    new Thread(() -> {
      try {
        serverSocket = new ServerSocket(port);

        do {
          LOGGER.info("Waiting for connection...");
          ClientConnection jointClientConnection = new JointClientConnection(
            serverSocket.accept(), nextClientId(), this::onConnectionEstablished, this::onDisconnect, this::onMessage
          );

          clientConnectionMap.put(jointClientConnection.getClientId(), jointClientConnection);

          if (isAllowedConnection(jointClientConnection)) {
            LOGGER.info("Client {} accepted", jointClientConnection.getClientId());
            new Thread(jointClientConnection).start();
          } else {
            LOGGER.info("Client {} rejected", jointClientConnection.getClientId());
            clientConnectionMap.remove(jointClientConnection.getClientId()).close();
          }

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

      for (ClientConnection clientConnection : clientConnectionMap.values()) {
        clientConnection.close();
      }
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }

  public void message(long id, SocketMessage socketMessage) {
    ClientConnection clientConnection = clientConnectionMap.get(id);

    if (clientConnection == null) {
      LOGGER.warn("ClientConnection with id {} does not exist", id);
    } else {
      try {
        clientConnection.sendMessage(socketMessage);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void broadcast(SocketMessage socketMessage, long... ignoreClients) {
    List<Long> ignoreIds = new ArrayList<>();

    if (ignoreClients != null) {
      for (long ignoreId : ignoreClients) {
        ignoreIds.add(ignoreId);
      }
    }

    clientConnectionMap.forEach((id, client) -> {
      if (ignoreIds.contains(id)) {
        return;
      }

      try {
        client.sendMessage(socketMessage);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private long nextClientId() {
    return clientCount++;
  }
}
