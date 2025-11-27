package technology.sola.engine.server;

import org.jspecify.annotations.NullMarked;
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

@NullMarked
class ClientConnectionImpl implements ClientConnection {
  private final NetworkQueue<SocketMessage> networkQueue = new NetworkQueue<>();
  private final Socket socket;
  private final long clientId;
  private final Consumer<ClientConnection> onConnectionEstablished;
  private final Consumer<ClientConnection> onDisconnect;
  private final ClientConnection.OnMessageHandler onMessage;
  private boolean isConnected = false;

  private final BufferedReader bufferedReader;
  private final PrintWriter printWriter;
  private final BufferedInputStream bufferedInputStream;
  private final BufferedOutputStream bufferedOutputStream;
  private final SocketMessageEncoder socketMessageEncoder = new SocketMessageEncoder();
  private final SocketMessageDecoder socketMessageDecoder = new SocketMessageDecoder();
  private boolean isWebSocketConnection = false;

  ClientConnectionImpl(Socket socket, long clientId, Consumer<ClientConnection> onConnectionEstablished, Consumer<ClientConnection> onDisconnect, ClientConnection.OnMessageHandler onMessage) {
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
      try {
        SocketMessage socketMessage = isWebSocketConnection
          ? socketMessageDecoder.decodeForWeb(bufferedInputStream)
          : socketMessageDecoder.decodeForRaw(bufferedInputStream);

        if (socketMessage == null) {
          isConnected = false;
          onDisconnect.accept(this);
        } else if (onMessage.accept(this, socketMessage)) {
          networkQueue.addLast(socketMessage);
        }
      } catch (IOException ex) {
        isConnected = false;
        onDisconnect.accept(this);
        throw new RuntimeException(ex);
      }
    }
  }

  @Override
  public void close() throws IOException {
    isConnected = false;

    socket.close();
    printWriter.close();
    bufferedReader.close();
    bufferedOutputStream.close();
    bufferedInputStream.close();
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

      try {
        String websocketAccept = Base64.getEncoder().encodeToString(
          MessageDigest.getInstance("SHA-1").digest((websocketKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes())
        );

        printWriter.write(
          "HTTP/1.1 101 Switching Protocols\r\n" +
            "Upgrade: websocket\r\n" +
            "Connection: Upgrade\r\n" +
            "Sec-WebSocket-Accept: " + websocketAccept + "\r\n\r\n"
        );
        printWriter.flush();
      } catch (NoSuchAlgorithmException ex) {
        throw new RuntimeException(ex);
      }
    }
  }
}
