package technology.sola.engine.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketMessage;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientConnection implements Runnable, Closeable {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnection.class);
  private final NetworkQueue<SocketMessage> networkQueue = new NetworkQueue<>();
  private final Socket socket;
  private final long clientId;
  private boolean isConnected = false;
  private ObjectInputStream objectInputStream = null;
  private ObjectOutputStream objectOutputStream = null;
  private Consumer<ClientConnection> onDisconnect;

  ClientConnection(Socket socket, long clientId, Consumer<ClientConnection> onDisconnect) {
    this.socket = socket;
    this.clientId = clientId;
    this.onDisconnect = onDisconnect;
  }

  public long getClientId() {
    return clientId;
  }

  public NetworkQueue<SocketMessage> getNetworkQueue() {
    return networkQueue;
  }

  public void sendMessage(SocketMessage socketMessage) throws IOException {
    objectOutputStream.writeObject(socketMessage);
    LOGGER.info("Message sent to {}", clientId);
  }

  @Override
  public void run() {
    isConnected = true;

    try {
      objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      objectInputStream = new ObjectInputStream(socket.getInputStream());

      while (isConnected) {
        LOGGER.info("Waiting for message");
        SocketMessage socketMessage = (SocketMessage) objectInputStream.readObject(); // blocking
        LOGGER.info("Message received from {} {}", clientId, socketMessage.toString());
        networkQueue.addLast(socketMessage);
      }
    } catch (EOFException ex) {
      onDisconnect.accept(this);
    } catch (IOException | ClassNotFoundException ex) {
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
}
