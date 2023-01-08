package technology.sola.engine.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class WebSocketUtils {
  private static final Random random = new Random();

  /**
   * Decodes a web socket encoded message from the client. Note, this is a blocking operation.
   *
   * @param bufferedInputStream the {@link BufferedInputStream} to read the input from
   * @return the decoded message
   * @throws IOException if an I/O error occurs
   */
  public static String decodeMessageFromClient(BufferedInputStream bufferedInputStream) throws IOException {
    byte[] starterBytes = bufferedInputStream.readNBytes(2);

    System.out.printf("%n%02x%n", starterBytes[0]);

    int length = 0xff & (starterBytes[1] - 0x80);
    System.out.println("length - " + length);

    byte[] key;

    if (length <= 125) {
      key = bufferedInputStream.readNBytes(4);
    } else {
      byte[] newLengthBytes = bufferedInputStream.readNBytes(2);
      int firstByteValue = 0xff & (newLengthBytes[0] << 8);
      int secondByteValue = 0xff & newLengthBytes[1];

      length = firstByteValue + secondByteValue;
      System.out.println("corrected length " + length);
      key = bufferedInputStream.readNBytes(4);
    }

    byte[] raw = bufferedInputStream.readNBytes(length);
    byte[] decoded = new byte[length];
    for (int i = 0; i < raw.length; i++) {
      decoded[i] = (byte) (raw[i] ^ key[i & 0x3]);
    }

    return new String(decoded, StandardCharsets.UTF_8);
  }

  /**
   * Formats a message to the client. Works for string size of <= 65535 bytes.
   *
   * @param string message to format for sending to the client
   * @return the formatted message
   */
  public static byte[] formatMessageToClient(String string) {
    byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

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

  public static byte[] encodeMessageToServer(String string) {
    byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

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
