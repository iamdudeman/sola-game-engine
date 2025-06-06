package technology.sola.engine.server;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.networking.socket.SocketMessage;

/**
 * SolaRestServer is a {@link SolaServer} without socket capabilities enabled.
 */
@NullMarked
public abstract class SolaRestServer extends SolaServer {
  /**
   * Creates an instance of this SolaServer.
   *
   * @param targetUpdatesPerSecond the target updates per second for the game loop
   */
  protected SolaRestServer(int targetUpdatesPerSecond) {
    super(targetUpdatesPerSecond);
  }

  @Override
  public int getSocketPort() {
    return -1;
  }

  @Override
  public boolean isAllowedConnection(ClientConnection clientConnection) {
    return false;
  }

  @Override
  public void onConnectionEstablished(ClientConnection clientConnection) {
    // no op
  }

  @Override
  public void onDisconnect(ClientConnection clientConnection) {
    // no op
  }

  @Override
  public boolean onMessage(ClientConnection clientConnection, SocketMessage socketMessage) {
    return false;
  }
}
