package technology.sola.engine.examples.server;

import technology.sola.engine.examples.common.networking.messages.MessageTypes;
import technology.sola.engine.examples.common.networking.messages.UpdateTimeMessage;
import technology.sola.engine.examples.common.networking.messages.old.PlayerUpdateMessageOld;
import technology.sola.engine.examples.common.networking.messages.old.RequestTimeMessageOld;
import technology.sola.engine.examples.common.networking.messages.old.UpdateTimeMessageOld;
import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.engine.networking.socket.SocketMessageOld;
import technology.sola.engine.server.ClientConnection;
import technology.sola.engine.server.SolaServer;

public class ServerMain {
  /**
   * Entry point for Server example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SolaServer solaServer = new ExampleSolaServer();

    solaServer.start(60000);
  }

  private static class ExampleSolaServer extends SolaServer {
    protected ExampleSolaServer() {
      super(30);
    }

    @Override
    public void initialize() {

    }

    @Override
    public boolean onConnect(ClientConnection clientConnection) {
      message(clientConnection.getClientId(), new UpdateTimeMessage());

//      message(clientConnection.getClientId(), new AssignPlayerIdMessageOld(clientConnection.getClientId()));
//      broadcast(new PlayerAddedMessageOld(clientConnection.getClientId()));

      return true;
    }

    @Override
    public void onDisconnect(ClientConnection clientConnection) {
      System.out.println("Disconnected - " + clientConnection.getClientId());

//      broadcast(new PlayerRemovedMessageOld(clientConnection.getClientId()), clientConnection.getClientId());
    }

    @Override
    public boolean onMessage(ClientConnection clientConnection, SocketMessage socketMessage) {
      System.out.println("Message received " + socketMessage);

      if (socketMessage.getType() == MessageTypes.REQUEST_TIME.ordinal()) {
        message(clientConnection.getClientId(), new UpdateTimeMessage());
      }

//      if (socketMessageOld instanceof RequestTimeMessageOld) {
//        message(clientConnection.getClientId(), new UpdateTimeMessageOld(System.currentTimeMillis()));
//      } else if (socketMessageOld instanceof PlayerUpdateMessageOld playerUpdateMessage) {
//        broadcast(playerUpdateMessage, clientConnection.getClientId());
//      }

      return true;
    }
  }
}
