package technology.sola.engine.platform.javafx.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketClient;
import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.engine.networking.socket.SocketMessageDecoder;
import technology.sola.engine.networking.socket.SocketMessageEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class JavaFxSocketClient implements SocketClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavaFxSocketClient.class);
  private boolean isConnected = false;
  private BufferedReader bufferedReader;
  private PrintWriter printWriter;
  private BufferedInputStream bufferedInputStream;
  private BufferedOutputStream bufferedOutputStream;
  private Socket socket;
  private final NetworkQueue<SocketMessage> networkQueue = new NetworkQueue<>();
  private final SocketMessageDecoder socketMessageDecoder = new SocketMessageDecoder();
  private final SocketMessageEncoder socketMessageEncoder = new SocketMessageEncoder();

  @Override
  public NetworkQueue<SocketMessage> getNetworkQueue() {
    return networkQueue;
  }

  @Override
  public void sendMessage(SocketMessage socketMessage) {
    if (!isConnected()) {
      LOGGER.warn("Connect send message when not connected");
      return;
    }

    try {
      bufferedOutputStream.write(socketMessageEncoder.encodeForRaw(socketMessage));
      bufferedOutputStream.flush();
    } catch (SocketException ex) {
      LOGGER.error("Something happened with the connection", ex);
      disconnect();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void connect(String host, int port) {
    if (isConnected()) {
      LOGGER.warn("Already connected to {}", socket.getInetAddress().toString());
      return;
    }

    try {
      socket = new Socket(host, port);

      new Thread(() -> {
        try {
          bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
          bufferedInputStream = new BufferedInputStream(socket.getInputStream());
          bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          printWriter = new PrintWriter(socket.getOutputStream());
          isConnected = true;

          printWriter.write("hello\r\n");
          printWriter.flush();

          while (isConnected) {
            SocketMessage socketMessage = socketMessageDecoder.decodeForRaw(bufferedInputStream);

            networkQueue.addLast(socketMessage);
          }
        } catch (SocketException ex) {
          // this happens when a disconnect happens
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      }).start();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void disconnect() {
    if (!isConnected()) {
      LOGGER.info("No connection active to disconnect");
      return;
    }

    try {
      LOGGER.info("Stopping connection to {}", socket.getInetAddress().toString());
      isConnected = false;
      if (socket != null) {
        socket.close();
      }
      if (bufferedReader != null) {
        bufferedReader.close();
      }
      if (printWriter != null) {
        printWriter.close();
      }
      if (bufferedInputStream != null) {
        bufferedInputStream.close();
      }
      if (bufferedOutputStream != null) {
        bufferedOutputStream.close();
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
