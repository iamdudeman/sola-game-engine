package technology.sola.engine.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.networking.NetworkQueue;
import technology.sola.engine.networking.socket.SocketMessage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Consumer;

class WebSocketClientConnection implements ClientConnection {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientConnection.class);
  private final NetworkQueue<SocketMessage> networkQueue = new NetworkQueue<>();
  private final Socket socket;
  private final long clientId;
  private final Consumer<ClientConnection> onConnect;
  private final Consumer<ClientConnection> onDisconnect;
  private final OnMessageHandler onMessage;
  private boolean isConnected = false;

  private BufferedReader bufferedReader;
  private PrintWriter printWriter;
  private BufferedInputStream bufferedInputStream;
  private BufferedOutputStream bufferedOutputStream;

  WebSocketClientConnection(Socket socket, long clientId, Consumer<ClientConnection> onConnect, Consumer<ClientConnection> onDisconnect, OnMessageHandler onMessage) {
    this.socket = socket;
    this.clientId = clientId;
    this.onConnect = onConnect;
    this.onDisconnect = onDisconnect;
    this.onMessage = onMessage;

    try {
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      bufferedInputStream = new BufferedInputStream(socket.getInputStream());
      bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
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
  public void sendMessage(SocketMessage socketMessage) throws IOException {
    bufferedOutputStream.write(WebSocketUtils.formatMessageToClient(socketMessage.toString()));
    bufferedOutputStream.flush();
    LOGGER.info("Message sent to {}", clientId);
  }

  @Override
  public void run() {
    // handshake
    try {
      String messageLine = bufferedReader.readLine(); // blocking

      System.out.println("messageline - " + messageLine);

      if (messageLine.startsWith("GET")) {
        while (!messageLine.startsWith("Sec-WebSocket-Key")) {
          messageLine = bufferedReader.readLine();
          System.out.println("messageline - " + messageLine);
        }

        String websocketKey = messageLine.replace("Sec-WebSocket-Key:", "").trim();

        while (!messageLine.isEmpty()) {
          System.out.println("discarded - " + messageLine);
          messageLine = bufferedReader.readLine();
        }

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
      }
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    // loop
    isConnected = true;

    try {
      onConnect.accept(this);

      while (isConnected) {
        LOGGER.info("Waiting for message");

        String decoded = WebSocketUtils.decodeMessageFromClient(bufferedInputStream);

        System.out.println("decoded - " + decoded);

        SocketMessage socketMessage = SocketMessage.fromString(decoded);

        if (onMessage.accept(this, socketMessage)) {
          LOGGER.info("Message received from {} {}", clientId, socketMessage.toString());
          networkQueue.addLast(socketMessage);
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
      if (bufferedOutputStream != null) {
        bufferedOutputStream.close();
      }
      if (bufferedReader != null) {
        bufferedReader.close();
      }
      if (bufferedInputStream != null) {
        bufferedInputStream.close();
      }
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }
}
