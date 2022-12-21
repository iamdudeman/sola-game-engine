package technology.sola.engine.platform.javafx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketClient;
import technology.sola.engine.networking.socket.SocketMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class JavaFxSocketClient implements SocketClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavaFxSocketClient.class);
  private boolean isConnected = false;
  private ObjectInputStream objectInputStream = null;
  private ObjectOutputStream objectOutputStream = null;
  private boolean isStarted = false;
  private Socket socket;
  private final NetworkQueue<SocketMessage> networkQueue = new NetworkQueue<>();

  @Override
  public void sendMessage(SocketMessage socketMessage) {
    try {
      objectOutputStream.writeObject(socketMessage);
      LOGGER.info("Message sent");
    } catch (IOException ex) {
      LOGGER.error("Failed to send message", ex);
    }
  }

  @Override
  public void connect(String host, int port) {
    isStarted = true;
    try {
      socket = new Socket(host, port);

      new Thread(() -> {
        try {
          objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
          objectInputStream = new ObjectInputStream(socket.getInputStream());
          isConnected = true;

          while (isConnected) {
            LOGGER.info("Waiting for message");
            SocketMessage socketMessage = (SocketMessage) objectInputStream.readObject(); // blocking
            LOGGER.info("Message received");
            networkQueue.addLast(socketMessage);
          }
        } catch (IOException | ClassNotFoundException ex) {
          LOGGER.error(ex.getMessage(), ex);
        }
      }).start();
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }

  @Override
  public void disconnect() {
    if (!isStarted) {
      return;
    }

    try {
      LOGGER.info("Stopping client");
      isConnected = false;
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

  @Override
  public boolean isConnected() {
    return isConnected;
  }
}
