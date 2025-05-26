package technology.sola.engine.networking.socket;

import org.jspecify.annotations.NullMarked;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * SocketMessageEncoder is responsible for encoding {@link SocketMessage}s for transmission over sockets.
 */
@NullMarked
public class SocketMessageEncoder {
  private final Random random = new Random();

  /**
   * Encodes a message for transmission over raw sockets. Works for string size of less than or equal to 65535 bytes.
   *
   * @param socketMessage {@link SocketMessage} to encode
   * @return encoded message
   */
  public byte[] encodeForRaw(SocketMessage socketMessage) {
    String message = socketMessage.toString();
    byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
    byte[] encoded = new byte[bytes.length + 2];

    encoded[1] = (byte) (bytes.length & 0xFF);
    encoded[0] = (byte) ((bytes.length >> 8) & 0xFF);
    System.arraycopy(bytes, 0, encoded, 2, bytes.length);

    return encoded;
  }

  /**
   * Encodes a message for transmission over web sockets. Works for string size of less than or equal to 65535 bytes. Does not mask payload.
   *
   * @param socketMessage {@link SocketMessage} to encode
   * @return encoded message
   */
  public byte[] encodeForWeb(SocketMessage socketMessage) {
    String message = socketMessage.toString();
    byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

    List<Byte> formattedBuilder = new ArrayList<>();

    formattedBuilder.add((byte) 0x81);

    if (bytes.length <= 125) {
      formattedBuilder.add((byte) bytes.length);
    } else {
      formattedBuilder.add((byte) 126);
      formattedBuilder.add((byte) (bytes.length >> 8 & 0xff));
      formattedBuilder.add((byte) (bytes.length & 0xff));
    }

    for (byte rawByte : bytes) {
      formattedBuilder.add(rawByte);
    }

    byte[] encoded = new byte[formattedBuilder.size()];

    for (int i = 0; i < formattedBuilder.size(); i++) {
      encoded[i] = formattedBuilder.get(i);
    }

    return encoded;
  }

  /**
   * Encodes a message for transmission over web sockets. Works for string size of less than or equal to 65535 bytes.
   *
   * @param socketMessage {@link SocketMessage} to encode
   * @return encoded message
   */
  public byte[] encodeForWebWithMask(SocketMessage socketMessage) {
    String message = socketMessage.toString();
    byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

    List<Byte> encodedBuilder = new ArrayList<>();

    encodedBuilder.add((byte) 0x81);
    encodedBuilder.add(
      (byte) (0x80 | (bytes.length < 125 ? bytes.length : 126))
    );

    if (bytes.length > 125) {
      byte[] buf = new byte[2];
      // 2-byte in network byte order.
      buf[1] = (byte) (bytes.length & 0xFF);
      buf[0] = (byte) ((bytes.length >> 8) & 0xFF);

      encodedBuilder.add(buf[0]);
      encodedBuilder.add(buf[1]);
    }

    byte[] key = new byte[4];
    random.nextBytes(key);

    for (byte keyByte : key) {
      encodedBuilder.add(keyByte);
    }

    for (int i = 0; i < bytes.length; i++) {
      encodedBuilder.add(
        (byte) ((bytes[i] ^ key[i % 4]) & 0xFF)
      );
    }


    byte[] encoded = new byte[encodedBuilder.size()];

    for (int i = 0; i < encodedBuilder.size(); i++) {
      encoded[i] = encodedBuilder.get(i);
    }

    return encoded;
  }
}
