package technology.sola.engine.server;

/**
 * SolaSocketServer is a {@link SolaServer} without REST capabilities enabled.
 */
public abstract class SolaSocketServer extends SolaServer {
  /**
   * Creates an instance of this SolaSocketServer.
   *
   * @param targetUpdatesPerSecond the target updates per second for the game loop
   */
  protected SolaSocketServer(int targetUpdatesPerSecond) {
    super(targetUpdatesPerSecond);
  }

  @Override
  public int getRestPort() {
    return -1;
  }
}
