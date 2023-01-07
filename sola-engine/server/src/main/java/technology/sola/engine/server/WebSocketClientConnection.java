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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Consumer;

// todo need to handle WebSockets if that is the connection type
//   https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_a_WebSocket_server_in_Java

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

  WebSocketClientConnection(Socket socket, long clientId, Consumer<ClientConnection> onConnect, Consumer<ClientConnection> onDisconnect, OnMessageHandler onMessage) {
    this.socket = socket;
    this.clientId = clientId;
    this.onConnect = onConnect;
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
    // handshake
    try {
      String messageLine = bufferedReader.readLine(); // blocking

      System.out.println("messageline - " + messageLine);

      if (messageLine.startsWith("GET")) {
        while (!messageLine.startsWith("Sec-WebSocket-Key")) {
          messageLine = bufferedReader.readLine();
          System.out.println("messageline - " + messageLine);
        }

        String thingy = messageLine.replace("Sec-WebSocket-Key:", "").trim();

        while (!messageLine.isEmpty()) {
          System.out.println("discarded - " + messageLine);
          messageLine = bufferedReader.readLine();
        }


//          byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
//    + "Connection: Upgrade\r\n"
//    + "Upgrade: websocket\r\n"
//    + "Sec-WebSocket-Accept: "
//    + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((thingy + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
//    + "\r\n\r\n").getBytes("UTF-8");

//        printWriter.write(response, 0, response.length);

        String blah = Base64.getEncoder().encodeToString(
          MessageDigest.getInstance("SHA-1").digest((thingy + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes())
        );

        printWriter.write(
          "HTTP/1.1 101 Switching Protocols\r\n" +
          "Upgrade: websocket\r\n" +
          "Connection: Upgrade\r\n" +
          "Sec-WebSocket-Accept: " + blah + "\r\n\r\n"
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
      // todo reenable in a bit
//      onConnect.accept(this);

      while (isConnected) {
        LOGGER.info("Waiting for message");

        String messageLine = bufferedReader.readLine(); // blocking

        System.out.println("message " + messageLine);

        if (messageLine == null) {
          isConnected = false;
          onDisconnect.accept(this);
        } else {
          decode(messageLine);

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

  private String decode(String encodedMessage) {
    /*
    byte 0: The 0x81 is just an indicator that a message received
    byte 1: the 0x8a is the length, substract 0x80 from it, 0x0A == 10
byte 2, 3, 4, 5: the 4 byte xor key to decrypt the payload
the rest: payload

     */

    byte[] bytes = encodedMessage.getBytes(StandardCharsets.UTF_8);


    StringBuilder sb = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02X ", b));
      sb2.append(b);
      sb2.append(" ");
    }
    System.out.println("bytes: " + sb.toString());
    System.out.println("bytes2: " + sb2.toString());


    int length = bytes[1] - 0x80;
    byte[] key = {
      bytes[2], bytes[3], bytes[4], bytes[5]
    };

    byte[] decoded = new byte[length];
    for (int i = 6; i < length; i++)
    {
      decoded[i] = (byte)(bytes[i] ^ key[i & 0x3]);
    }

    System.out.println("result " + new String(decoded, StandardCharsets.UTF_8));

    return null;
  }

  private static String decode(byte[] bytes) {
    System.out.printf("%02x%n", bytes[0]);

    int length = 0xff & (bytes[1] - 0x80);
    System.out.println("length - " + length);

    int offset = 6;
    byte[] key;

    if (length < 125) {
      key = new byte[] {
        bytes[2], bytes[3], bytes[4], bytes[5]
      };
    } else {
      System.out.printf("%02x%n", bytes[1]);

      offset = 8;
      length = (bytes[2] << 8) + bytes[3];
      key = new byte[] {
        bytes[4], bytes[5], bytes[6], bytes[7]
      };
    }

    byte[] decoded = new byte[length];
    for (int i = offset; i < bytes.length; i++)
    {
      int decodedIndex = i - offset;
      decoded[decodedIndex] = (byte)(bytes[i] ^ key[decodedIndex & 0x3]);
    }

    return new String(decoded, StandardCharsets.UTF_8);
  }
}
