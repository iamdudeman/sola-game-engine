package technology.sola.engine.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketMessage;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

class RawSocketClientConnection implements ClientConnection {
  private static final Logger LOGGER = LoggerFactory.getLogger(RawSocketClientConnection.class);
  private final NetworkQueue<SocketMessage> networkQueue = new NetworkQueue<>();
  private final Socket socket;
  private final long clientId;
  private final Consumer<ClientConnection> onDisconnect;
  private final OnMessageHandler onMessage;
  private boolean isConnected = false;

  private BufferedReader bufferedReader;
  private PrintWriter printWriter;

  RawSocketClientConnection(Socket socket, long clientId, Consumer<ClientConnection> onDisconnect, OnMessageHandler onMessage) {
    this.socket = socket;
    this.clientId = clientId;
    this.onDisconnect = onDisconnect;
    this.onMessage = onMessage;

    try {
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      printWriter = new PrintWriter(socket.getOutputStream());
    } catch (IOException ex) {
      // todo
      LOGGER.error("Something went wrong establishing connection", ex);
    }
  }

  @Override
  public long getClientId() {
    return clientId;
  }

  @Override
  public NetworkQueue<SocketMessage> getNetworkQueue() {
    return networkQueue;
  }

  @Override
  public void sendMessage(SocketMessage socketMessage) {
    printWriter.write(socketMessage.toString());
    printWriter.flush();
    LOGGER.info("Message sent to {}", clientId);
  }

  @Override
  public void run() {
    // todo need to implement a simple handshake of some sort
    // todo then need to join this together with WebSocketClientConnection

    isConnected = true;

    try {
      while (isConnected) {
        LOGGER.info("Waiting for message");

        String messageLine = bufferedReader.readLine(); // blocking

        System.out.println("message " + messageLine);

        if (messageLine == null) {
          isConnected = false;
          onDisconnect.accept(this);
        } else {
          SocketMessage socketMessage = SocketMessage.fromString(messageLine);

          if (onMessage.accept(this, socketMessage)) {
            LOGGER.info("Message received from {} {}", clientId, socketMessage.toString());
            networkQueue.addLast(socketMessage);
          }
        }
      }
    } catch (EOFException ex) {
      LOGGER.info("Disconnected {}", clientId);
      isConnected = false;
      onDisconnect.accept(this);
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }

  @Override
  public void close() {
    isConnected = false;

    try {
      if (socket != null) {
        socket.close();
      }
      if (printWriter != null) {
        printWriter.close();
      }
      if (bufferedReader != null) {
        bufferedReader.close();
      }
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }
}
