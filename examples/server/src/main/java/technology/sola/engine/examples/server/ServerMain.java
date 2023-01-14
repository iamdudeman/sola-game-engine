package technology.sola.engine.examples.server;

import technology.sola.engine.examples.common.networking.messages.AssignPlayerIdMessage;
import technology.sola.engine.examples.common.networking.messages.MessageType;
import technology.sola.engine.examples.common.networking.messages.PlayerAddedMessage;
import technology.sola.engine.examples.common.networking.messages.PlayerRemovedMessage;
import technology.sola.engine.examples.common.networking.messages.UpdateTimeMessage;
import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.engine.server.ClientConnection;
import technology.sola.engine.server.SolaServer;

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

    solaServer.start(1380);
  }

  private static class ExampleSolaServer extends SolaServer {
    protected ExampleSolaServer() {
      super(30);
    }

    @Override
    public void initialize() {

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
