package technology.sola.engine.platform.swing.core;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketClient;
import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.engine.networking.socket.SocketMessageDecoder;
import technology.sola.engine.networking.socket.SocketMessageEncoder;
import technology.sola.logging.SolaLogger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * A Swing implementation of {@link SocketClient}.
 */
@NullMarked
public class SwingSocketClient implements SocketClient {
  private static final SolaLogger LOGGER = SolaLogger.of(SwingSocketClient.class);
  private boolean isConnected = false;
  @Nullable
  private BufferedReader bufferedReader;
  @Nullable
  private PrintWriter printWriter;
  @Nullable
  private BufferedInputStream bufferedInputStream;
  @Nullable
  private BufferedOutputStream bufferedOutputStream;
  @Nullable
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
      LOGGER.warning("Connect send message when not connected");
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
      LOGGER.warning("Already connected to %s", socket.getInetAddress().toString());
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
      LOGGER.info("Stopping connection to %s", socket.getInetAddress().toString());
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
