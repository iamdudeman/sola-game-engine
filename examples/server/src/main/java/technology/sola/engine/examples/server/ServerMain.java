package technology.sola.engine.examples.server;

import technology.sola.engine.server.SocketServer;

public class ServerMain {
  /**
   * Entry point for Server example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    // todo remove hard coded socket server usage here
    new SocketServer().start(60000);
  }
}
