package technology.sola.engine.examples.server;

import technology.sola.engine.examples.common.networking.messages.AssignPlayerIdMessage;
import technology.sola.engine.examples.common.networking.messages.MessageType;
import technology.sola.engine.examples.common.networking.messages.PlayerAddedMessage;
import technology.sola.engine.examples.common.networking.messages.PlayerRemovedMessage;
import technology.sola.engine.examples.common.networking.messages.UpdateTimeMessage;
import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.engine.server.ClientConnection;
import technology.sola.engine.server.SolaServer;
import technology.sola.engine.server.rest.SolaResponse;
import technology.sola.json.JsonObject;

/**
 * Runs the example server listening on port 60000.
 */
public class ServerMain {
  /**
   * Entry point for Server example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SolaServer solaServer = new ExampleSolaServer();

    solaServer.start();
  }

  private static class ExampleSolaServer extends SolaServer {
    protected ExampleSolaServer() {
      super(30);
    }

    @Override
    public void initialize() {
      solaRouter.route("GET", "/", requestParameters -> new SolaResponse(200, new JsonObject()));

      // todo test route for path params

      solaRouter.route("GET", "/test", requestParameters -> {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("test", requestParameters.queryParameters().getOrDefault("test", "missing"));

        return new SolaResponse(200, jsonObject);
      });

      solaRouter.route("POST", "/test", requestParameters -> new SolaResponse(200, requestParameters.body()));
    }

    @Override
    public int getRestPort() {
      return 1381;
    }

    @Override
    public int getSocketPort() {
      return 1380;
    }

    @Override
    public boolean isAllowedConnection(ClientConnection clientConnection) {
      return true;
    }

    @Override
    public void onConnectionEstablished(ClientConnection clientConnection) {
      message(clientConnection.getClientId(), new UpdateTimeMessage(System.currentTimeMillis()));
      message(clientConnection.getClientId(), new AssignPlayerIdMessage(clientConnection.getClientId()));
      broadcast(new PlayerAddedMessage(clientConnection.getClientId()));
    }

    @Override
    public void onDisconnect(ClientConnection clientConnection) {
      System.out.println("Disconnected - " + clientConnection.getClientId());

      broadcast(new PlayerRemovedMessage(clientConnection.getClientId()), clientConnection.getClientId());
    }

    @Override
    public boolean onMessage(ClientConnection clientConnection, SocketMessage socketMessage) {
      MessageType messageType = MessageType.values()[socketMessage.getType()];

      switch (messageType) {
        case REQUEST_TIME -> message(clientConnection.getClientId(), new UpdateTimeMessage(System.currentTimeMillis()));
        case PLAYER_UPDATE -> broadcast(socketMessage, clientConnection.getClientId());
      }

      return true;
    }
  }
}
