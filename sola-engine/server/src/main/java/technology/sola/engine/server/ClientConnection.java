package technology.sola.engine.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketMessage;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

// todo need to handle WebSockets if that is the connection type
//   https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_a_WebSocket_server_in_Java

public class ClientConnection implements Runnable, Closeable {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnection.class);
  private final NetworkQueue<SocketMessage> networkQueue = new NetworkQueue<>();
  private final Socket socket;
  private final long clientId;
  private final Consumer<ClientConnection> onDisconnect;
  private final OnMessageHandler onMessage;
  private boolean isConnected = false;
  private ObjectInputStream objectInputStream = null;
  private ObjectOutputStream objectOutputStream = null;

  private BufferedReader bufferedReader;
  private PrintWriter printWriter;

  ClientConnection(Socket socket, long clientId, Consumer<ClientConnection> onDisconnect, OnMessageHandler onMessage) {
    this.socket = socket;
    this.clientId = clientId;
    this.onDisconnect = onDisconnect;
    this.onMessage = onMessage;

    try {
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      printWriter = new PrintWriter(socket.getOutputStream());

//      objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//      objectInputStream = new ObjectInputStream(socket.getInputStream());
    } catch (IOException ex) {
      // todo
      LOGGER.error("Something went wrong establishing connection", ex);
    }
  }

  public long getClientId() {
    return clientId;
  }

  public NetworkQueue<SocketMessage> getNetworkQueue() {
    return networkQueue;
  }

  public void sendMessage(SocketMessage socketMessage) throws IOException {
//    printWriter.write("some text");
//    printWriter.flush();
//    objectOutputStream.writeObject(socketMessage);
    LOGGER.info("Message sent to {}", clientId);
  }

  @Override
  public void run() {
    isConnected = true;

    try {
      while (isConnected) {
        LOGGER.info("Waiting for message");

        String line = bufferedReader.readLine();

        System.out.println("read " + line);

        if (line == null) {
          isConnected = false;
          onDisconnect.accept(this);
        } else {
          System.out.println("read " + line);
        }

//        SocketMessage socketMessage = (SocketMessage) objectInputStream.readObject(); // blocking
//
//        if (onMessage.accept(this, socketMessage)) {
//          LOGGER.info("Message received from {} {}", clientId, socketMessage.toString());
//          networkQueue.addLast(socketMessage);
//        }
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
      if (objectInputStream != null) {
        objectInputStream.close();
      }
      if (objectOutputStream != null) {
        objectOutputStream.close();
      }
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }

  @FunctionalInterface
  interface OnMessageHandler {
    boolean accept(ClientConnection clientConnection, SocketMessage socketMessage);
  }
}
