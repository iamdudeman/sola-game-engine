package technology.sola.engine.networking.socket;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.networking.NetworkQueue;

/**
 * SocketClient defines the api for interacting with a socket server.
 */
@NullMarked
public interface SocketClient {
  /**
   * @return the queue of messages from the server
   */
  NetworkQueue<SocketMessage> getNetworkQueue();

  /**
   * Sends a message to the server.
   *
   * @param socketMessage the {@link SocketMessage} to send
   */
  void sendMessage(SocketMessage socketMessage);

  /**
   * Connects to a server by host and port.
   *
   * @param host the host of the server
   * @param port the port accepting connections
   */
  void connect(String host, int port);

  /**
   * Disconnect from the server.
   */
  void disconnect();

  /**
   * @return true if connected to a server
   */
  boolean isConnected();
}
