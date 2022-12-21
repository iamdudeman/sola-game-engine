package technology.sola.engine.platform.server;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.renderer.Renderer;

public abstract class ServerSola extends Sola {
  protected SocketServer socketServer = new SocketServer();

  public ServerSola(int targetUpdatesPerSecond) {
    super(SolaConfiguration.build("Server", 1, 1).withTargetUpdatesPerSecond(targetUpdatesPerSecond).build());
  }

  @Override
  protected void onRender(Renderer renderer) {
    // Nothing needed
  }
}
