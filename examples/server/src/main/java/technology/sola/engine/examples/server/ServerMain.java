package technology.sola.engine.examples.server;

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
      return true;
    }

    @Override
    public void onDisconnect(ClientConnection clientConnection) {
      System.out.println("Disconnected - " + clientConnection.getClientId());
    }

    @Override
    public boolean onMessage(ClientConnection clientConnection, SocketMessage socketMessage) {
      return true;
    }
  }
}
