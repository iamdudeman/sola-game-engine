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
  private final Consumer<ClientConnection> onConnect;
  private final Consumer<ClientConnection> onDisconnect;
  private final OnMessageHandler onMessage;
  private boolean isConnected = false;

  private BufferedReader bufferedReader;
  private PrintWriter printWriter;
  private BufferedInputStream bufferedInputStream;
  private BufferedOutputStream bufferedOutputStream;
  private final SocketMessageEncoder socketMessageEncoder = new SocketMessageEncoder();
  private final SocketMessageDecoder socketMessageDecoder = new SocketMessageDecoder();

  RawSocketClientConnection(Socket socket, long clientId, Consumer<ClientConnection> onConnect, Consumer<ClientConnection> onDisconnect, OnMessageHandler onMessage) {
    this.socket = socket;
    this.clientId = clientId;
    this.onConnect = onConnect;
    this.onDisconnect = onDisconnect;
    this.onMessage = onMessage;

    try {
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      printWriter = new PrintWriter(socket.getOutputStream());
      bufferedInputStream = new BufferedInputStream(socket.getInputStream());
      bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
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
    try {
      bufferedOutputStream.write(socketMessageEncoder.encodeForRaw(socketMessage));
      bufferedOutputStream.flush();
      LOGGER.info("Message sent to {}", clientId);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    // todo then need to join this together with WebSocketClientConnection
    // handshake
    try {
      String messageLine = bufferedReader.readLine();

      System.out.println("handshake - " + messageLine);

      if (!messageLine.startsWith("hello")) {
        onDisconnect.accept(this);
        return;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    isConnected = true;
    onConnect.accept(this);

    try {
      while (isConnected) {
        LOGGER.info("Waiting for message");

        // todo consider replacing this readLine with read bytes so newline character isn't required?
        SocketMessage socketMessage = socketMessageDecoder.decodeForRaw(bufferedInputStream);

        System.out.println("received - " + socketMessage);


//        String messageLine = bufferedReader.readLine(); // blocking

//        if (messageLine == null) {
//          isConnected = false;
//          onDisconnect.accept(this);
//        } else {
//          SocketMessage socketMessage = SocketMessage.fromString(messageLine);

          if (onMessage.accept(this, socketMessage)) {
            LOGGER.info("Message received from {} {}", clientId, socketMessage.toString());
            networkQueue.addLast(socketMessage);
          }
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
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }
}
