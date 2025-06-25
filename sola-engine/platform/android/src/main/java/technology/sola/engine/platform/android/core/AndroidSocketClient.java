package technology.sola.engine.platform.android.core;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketClient;
import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.logging.SolaLogger;

/**
 * {@link technology.sola.engine.platform.android.AndroidSolaPlatform} implementation of {@link SocketClient}.
 */
@NullMarked
public class AndroidSocketClient implements SocketClient {
  private static final SolaLogger LOGGER = SolaLogger.of(AndroidSocketClient.class);

  @Override
  public NetworkQueue<SocketMessage> getNetworkQueue() {
    // todo implement
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void sendMessage(SocketMessage socketMessage) {
    // todo implement
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void connect(String host, int port) {
    // todo implement
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void disconnect() {
    if (!isConnected()) {
      LOGGER.info("No connection active to disconnect");
      return;
    }

    // todo implement
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isConnected() {
    // todo implement
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
