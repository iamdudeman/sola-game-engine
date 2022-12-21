package technology.sola.engine.examples.server;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.Sola;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.platform.server.ServerSolaPlatform;
import technology.sola.engine.platform.server.SocketServer;

/**
 * Runs the {@link ExampleLauncherSola} on the {@link ServerSolaPlatform}.
 */
public class ServerMain {
  /**
   * Entry point for Swing example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
//    SolaPlatform solaPlatform = new ServerSolaPlatform();
//    Sola sola = new ExampleServerSola(30);
//
//    solaPlatform.play(sola);

    // todo remove hard coded socket server usage here
    new SocketServer().start(60000);
  }
}
