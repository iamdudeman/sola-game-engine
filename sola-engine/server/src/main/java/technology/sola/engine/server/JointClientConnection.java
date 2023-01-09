package technology.sola.engine.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.networking.NetworkQueue;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Consumer;

class JointClientConnection implements ClientConnection {
  private static final Logger LOGGER = LoggerFactory.getLogger(JointClientConnection.class);
  private final NetworkQueue<SocketMessage> networkQueue = new NetworkQueue<>();
  private final Socket socket;
  private final long clientId;
  private final Consumer<ClientConnection> onConnectionEstablished;
  private final Consumer<ClientConnection> onDisconnect; // todo hook this up
  private final ClientConnection.OnMessageHandler onMessage;
  private boolean isConnected = false;

  private final BufferedReader bufferedReader;
  private final PrintWriter printWriter;
  private final BufferedInputStream bufferedInputStream;
  private final BufferedOutputStream bufferedOutputStream;
  private final SocketMessageEncoder socketMessageEncoder = new SocketMessageEncoder();
  private final SocketMessageDecoder socketMessageDecoder = new SocketMessageDecoder();
  private boolean isWebSocketConnection = false;

  JointClientConnection(Socket socket, long clientId, Consumer<ClientConnection> onConnectionEstablished, Consumer<ClientConnection> onDisconnect, ClientConnection.OnMessageHandler onMessage) {
    this.socket = socket;
    this.clientId = clientId;
    this.onConnectionEstablished = onConnectionEstablished;
    this.onDisconnect = onDisconnect;
    this.onMessage = onMessage;

    try {
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      printWriter = new PrintWriter(socket.getOutputStream());
      bufferedInputStream = new BufferedInputStream(socket.getInputStream());
      bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
    } catch (IOException ex) {
      throw new RuntimeException(ex);
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
  public void sendMessage(SocketMessage socketMessage) throws IOException {
    byte[] bytes = isWebSocketConnection
      ? socketMessageEncoder.encodeForWeb(socketMessage)
      : socketMessageEncoder.encodeForRaw(socketMessage);

    bufferedOutputStream.write(bytes);
    bufferedOutputStream.flush();
    LOGGER.info("Message sent to {}", clientId);
  }

  @Override
  public void run() {
    try {
      handshake();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    isConnected = true;

    onConnectionEstablished.accept(this);

    while (isConnected) {
      LOGGER.info("Waiting for message");

      SocketMessage socketMessage = null;
      try {
        socketMessage = isWebSocketConnection
          ? socketMessageDecoder.decodeForWeb(bufferedInputStream)
          : socketMessageDecoder.decodeForRaw(bufferedInputStream);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }

      if (onMessage.accept(this, socketMessage)) {
        LOGGER.info("Message received from {} {}", clientId, socketMessage.toString());
        networkQueue.addLast(socketMessage);
      }
    }
  }

  @Override
  public void close() throws IOException {
    isConnected = false;

    if (socket != null) {
      socket.close();
    }
    if (printWriter != null) {
      printWriter.close();
    }
    if (bufferedReader != null) {
      bufferedReader.close();
    }
    if (bufferedOutputStream != null) {
      bufferedOutputStream.close();
    }
    if (bufferedInputStream != null) {
      bufferedInputStream.close();
    }
  }

  private void handshake() throws IOException {
    String messageLine = bufferedReader.readLine(); // blocking

    if (messageLine.startsWith("hello")) {
      isWebSocketConnection = false;
    } else if (messageLine.startsWith("GET")) {
      isWebSocketConnection = true;

      while (!messageLine.startsWith("Sec-WebSocket-Key")) {
        messageLine = bufferedReader.readLine();
      }

      String websocketKey = messageLine.replace("Sec-WebSocket-Key:", "").trim();

      while (!messageLine.isEmpty()) {
        messageLine = bufferedReader.readLine();
      }

      String websocketAccept = null;
      try {
        websocketAccept = Base64.getEncoder().encodeToString(
          MessageDigest.getInstance("SHA-1").digest((websocketKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes())
        );
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }

      printWriter.write(
        "HTTP/1.1 101 Switching Protocols\r\n" +
          "Upgrade: websocket\r\n" +
          "Connection: Upgrade\r\n" +
          "Sec-WebSocket-Accept: " + websocketAccept + "\r\n\r\n"
      );
      printWriter.flush();
    }
  }
}
