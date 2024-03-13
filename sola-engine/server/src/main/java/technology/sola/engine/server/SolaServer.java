package technology.sola.engine.server;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.ecs.SolaEcs;
import technology.sola.engine.core.GameLoop;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.engine.server.rest.SolaRouter;
import technology.sola.json.JsonElement;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * SolaServer handles network traffic to and from many client connections.
 */
public abstract class SolaServer {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolaServer.class);
  /**
   * {@link SolaEcs} instance used by the server's game loop.
   */
  protected final SolaEcs solaEcs;
  /**
   * {@link EventHub} instance used by the server's game loop.
   */
  protected final EventHub eventHub;
  protected final SolaRouter solaRouter = new SolaRouter();
  private final Map<Long, ClientConnection> clientConnectionMap = new HashMap<>();
  private long clientCount = 0;
  private ServerSocket serverSocket;
  private boolean isAcceptingConnections = true;
  private boolean isStarted = false;
  private final GameLoop gameLoop;

  /**
   * Creates an instance of this SolaServer.
   *
   * @param targetUpdatesPerSecond the target updates per second for the game loop
   */
  protected SolaServer(int targetUpdatesPerSecond) {
    solaEcs = new SolaEcs();
    eventHub = new EventHub();
    this.gameLoop = new ServerGameLoop(eventHub, solaEcs::updateWorld, targetUpdatesPerSecond);
  }

  /**
   * Called when the server is starting up to initialize things.
   */
  public abstract void initialize();

  /**
   * @return the port listening for http requests
   */
  public abstract int getRestPort();


  /**
   * @return the port listening for socket connections
   */
  public abstract int getSocketPort();

  /**
   * Called when a new connection is attempted to accept or reject it.
   *
   * @param clientConnection the {@link ClientConnection} that is connecting
   * @return true to allow connection
   */
  public abstract boolean isAllowedConnection(ClientConnection clientConnection);

  /**
   * Called when a new connection has been established and messages can be sent.
   *
   * @param clientConnection the {@link ClientConnection} that is now established
   */
  public abstract void onConnectionEstablished(ClientConnection clientConnection);

  /**
   * Called when a client disconnects.
   *
   * @param clientConnection the {@link ClientConnection} that is disconnecting
   */
  public abstract void onDisconnect(ClientConnection clientConnection);

  /**
   * Called whenever a message is received from a client. If the message is valid returning true will add it to the
   * {@link technology.sola.engine.networking.NetworkQueue} for the client's connection.
   *
   * @param clientConnection the {@link ClientConnection} that sent the message
   * @param socketMessage    the {@link SocketMessage} that was sent
   * @return true if message should be added to the queue
   */
  public abstract boolean onMessage(ClientConnection clientConnection, SocketMessage socketMessage);

  /**
   * @return the map of client connections
   */
  public Map<Long, ClientConnection> getClientConnectionMap() {
    return clientConnectionMap;
  }

  /**
   * Starts the server.
   */
  public void start() {
    int restPort = getRestPort();
    int socketPort = getSocketPort();

    initialize();

    isStarted = true;

    // game loop thread
    new Thread(gameLoop).start();

    // rest thread
    if (restPort != -1) {
      new Thread(() -> {
        try {
          HttpServer httpServer = HttpServer.create(new InetSocketAddress(restPort), 0);

          httpServer.createContext("/", exchange -> {
            var response = solaRouter.handleRequest(exchange);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(response.status(), 0);

            try (PrintStream printStream = new PrintStream(exchange.getResponseBody())) {
              printStream.print(response.body().toString());
            }
          });

          httpServer.setExecutor(Executors.newCachedThreadPool());
          httpServer.start();
          LOGGER.info("Started REST server on port " + restPort);
        } catch (IOException ex) {
          // todo handle this
          ex.printStackTrace();
        }
      }).start();
    }

    // socket thread
    if (socketPort != -1) {
      new Thread(() -> {
        try {
          serverSocket = new ServerSocket(socketPort);

          do {
            LOGGER.info("Waiting for connection...");
            ClientConnection jointClientConnection = new ClientConnectionImpl(
              serverSocket.accept(), nextClientId(), this::onConnectionEstablished, this::handleDisconnect, this::onMessage
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
  }

  /**
   * Stops the server.
   */
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

  /**
   * Sends a message to a specific client.
   *
   * @param id            the client's id
   * @param socketMessage the {@link SocketMessage} to send
   */
  public void message(long id, SocketMessage socketMessage) {
    ClientConnection clientConnection = clientConnectionMap.get(id);

    if (clientConnection == null) {
      LOGGER.warn("ClientConnection with id {} does not exist", id);
    } else {
      if (!messageClient(clientConnection, socketMessage)) {
        onDisconnect(clientConnection);
        clientConnectionMap.remove(id);
      }
    }
  }

  /**
   * Sends a message to all connected clients excluding ignored clients.
   *
   * @param socketMessage the {@link SocketMessage} to send
   * @param ignoreClients the client ids to ignore sending the message to
   */
  public void broadcast(SocketMessage socketMessage, long... ignoreClients) {
    List<Long> ignoreIds = new ArrayList<>();

    if (ignoreClients != null) {
      for (long ignoreId : ignoreClients) {
        ignoreIds.add(ignoreId);
      }
    }

    for (var iter = clientConnectionMap.entrySet().iterator(); iter.hasNext(); ) {
      var entry = iter.next();

      if (ignoreIds.contains(entry.getKey())) {
        continue;
      }

      if (!messageClient(entry.getValue(), socketMessage)) {
        onDisconnect(entry.getValue());
        iter.remove();
      }
    }
  }

  private boolean messageClient(ClientConnection clientConnection, SocketMessage socketMessage) {
    try {
      clientConnection.sendMessage(socketMessage);
    } catch (SocketException ex) {
      LOGGER.error("Error sending message to {}", clientConnection.getClientId(), ex);
      return false;
    } catch (IOException ex) {
      LOGGER.error("Error sending message to {}", clientConnection.getClientId(), ex);
    }

    return true;
  }

  private long nextClientId() {
    return clientCount++;
  }

  private void handleDisconnect(ClientConnection clientConnection) {
    try {
      onDisconnect(clientConnection);
      clientConnectionMap.remove(clientConnection.getClientId()).close();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
