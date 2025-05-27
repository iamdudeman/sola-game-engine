package technology.sola.engine.platform.browser.core;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketClient;
import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.engine.platform.browser.javascript.JsNetworkUtils;

/**
 * A browser implementation of {@link SocketClient}.
 */
@NullMarked
public class BrowserSocketClient implements SocketClient {
  private final NetworkQueue<SocketMessage> networkQueue = new NetworkQueue<>();
  private boolean isConnected = false;

  @Override
  public NetworkQueue<SocketMessage> getNetworkQueue() {
    return networkQueue;
  }

  @Override
  public void sendMessage(SocketMessage socketMessage) {
    JsNetworkUtils.sendMessage(socketMessage.toString());
  }

  @Override
  public void connect(String host, int port) {
    JsNetworkUtils.connectSocket(host, port, (messageData) -> {
      SocketMessage socketMessage = SocketMessage.parse(messageData);

      networkQueue.addLast(socketMessage);
    });
    isConnected = true;
  }

  @Override
  public void disconnect() {
    JsNetworkUtils.disconnect();
    isConnected = false;
  }

  @Override
  public boolean isConnected() {
    return isConnected;
  }
}
