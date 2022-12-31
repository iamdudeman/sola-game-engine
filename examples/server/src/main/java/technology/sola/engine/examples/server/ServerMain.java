package technology.sola.engine.examples.server;

import technology.sola.engine.examples.common.networking.RequestTimeMessage;
import technology.sola.engine.examples.common.networking.UpdateTimeMessage;
import technology.sola.engine.networking.socket.SocketMessage;
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
      message(clientConnection.getClientId(), new UpdateTimeMessage(System.currentTimeMillis()));

      return true;
    }

    @Override
    public void onDisconnect(ClientConnection clientConnection) {
      System.out.println("Disconnected - " + clientConnection.getClientId());
    }

    @Override
    public boolean onMessage(ClientConnection clientConnection, SocketMessage socketMessage) {
      System.out.println("Message received " + socketMessage.getClass());

      if (socketMessage instanceof RequestTimeMessage) {
        message(clientConnection.getClientId(), new UpdateTimeMessage(System.currentTimeMillis()));
      }

      return true;
    }
  }
}
