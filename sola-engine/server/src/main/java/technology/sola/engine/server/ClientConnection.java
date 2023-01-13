package technology.sola.engine.server;

import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketMessage;

import java.io.Closeable;
import java.io.IOException;

/**
 * ClientConnection is a handle to the client's connection to the server.
 */
public interface ClientConnection extends Runnable, Closeable {
  /**
   * @return the id for the client
   */
  long getClientId();

  /**
   * @return the queue of messages from the server
   */
  NetworkQueue<SocketMessage> getNetworkQueue();

  /**
   * Sends a message to the server.
   *
   * @param socketMessage the {@link SocketMessage} to send
   * @throws IOException if an I/O error occurs.
   */
  void sendMessage(SocketMessage socketMessage) throws IOException;

  /**
   * OnMessageHandler is a functional interface for handling when the server receives a message from a client.
   */
  @FunctionalInterface
  interface OnMessageHandler {
    /**
     * Called whenever a client sends a message to the server to determine if the message should be accepted or not.
     *
     * @param clientConnection the {@link ClientConnection} that sent the message
     * @param socketMessage    the {@link SocketMessage} sent by the client
     * @return true if the message should be handled by the server
     */
    boolean accept(ClientConnection clientConnection, SocketMessage socketMessage);
  }
}
