package technology.sola.engine.platform.android.core;

import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketClient;
import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.logging.SolaLogger;

public class AndroidSocketClient implements SocketClient {
  private static final SolaLogger LOGGER = SolaLogger.of(AndroidSocketClient.class);

  @Override
  public NetworkQueue<SocketMessage> getNetworkQueue() {
    // todo
    return null;
  }

  @Override
  public void sendMessage(SocketMessage socketMessage) {
    // todo
  }

  @Override
  public void connect(String host, int port) {
    // todo
  }

  @Override
  public void disconnect() {
    if (!isConnected()) {
      LOGGER.info("No connection active to disconnect");
      return;
    }


    // todo
  }

  @Override
  public boolean isConnected() {
    // todo
    return false;
  }
}
