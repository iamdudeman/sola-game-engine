package technology.sola.engine.examples.server;

import technology.sola.engine.platform.server.ServerSola;
import technology.sola.engine.platform.server.SocketServer;

public class ExampleServerSola extends ServerSola {
  public ExampleServerSola(int targetUpdatesPerSecond) {
    super(targetUpdatesPerSecond);
  }

  @Override
  protected void onInit() {

  }
}
