package technology.sola.engine.platform.browser.core;

import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketClient;
import technology.sola.engine.networking.socket.SocketMessageOld;
import technology.sola.engine.platform.browser.javascript.JsNetworkUtils;

public class BrowserSocketClient implements SocketClient {
  private final NetworkQueue<SocketMessageOld> networkQueue = new NetworkQueue<>();
  private boolean isConnected = false;

  @Override
  public NetworkQueue<SocketMessageOld> getNetworkQueue() {
    return networkQueue;
  }

  @Override
  public void sendMessage(SocketMessageOld socketMessageOld) {
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public void connect(String host, int port) {
    JsNetworkUtils.connectSocket(host, port);
    // todo need to hook up adding messages to NetworkQueue
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
