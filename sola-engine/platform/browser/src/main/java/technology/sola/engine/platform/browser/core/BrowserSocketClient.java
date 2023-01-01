package technology.sola.engine.platform.browser.core;

import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketClient;
import technology.sola.engine.networking.socket.SocketMessage;

public class BrowserSocketClient implements SocketClient {
  private NetworkQueue<SocketMessage> networkQueue;

  @Override
  public NetworkQueue<SocketMessage> getNetworkQueue() {
    return networkQueue;
  }

  @Override
  public void sendMessage(SocketMessage socketMessage) {

  }

  @Override
  public void connect(String host, int port) {

  }

  @Override
  public void disconnect() {

  }

  @Override
  public boolean isConnected() {
    return false;
  }
}
