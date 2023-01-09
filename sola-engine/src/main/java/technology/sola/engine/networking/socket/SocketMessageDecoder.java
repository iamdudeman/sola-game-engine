package technology.sola.engine.networking.socket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * SocketMessageDecoder is responsible for decoding {@link SocketMessage}s from payloads received over sockets.
 */
public class SocketMessageDecoder {
  /**
   * Decodes a raw socket encoded message from the client. Note, this is a blocking operation.
   *
   * @param bufferedInputStream the {@link BufferedInputStream} to read the input from
   * @return the decoded message or null if disconnected
   * @throws IOException if an I/O error occurs
   */
  public SocketMessage decodeForRaw(BufferedInputStream bufferedInputStream) throws IOException {
    byte[] lengthBytes = bufferedInputStream.readNBytes(2);

    if (lengthBytes.length == 0) {
      return null;
    }

    int firstByteValue = 0xff & (lengthBytes[0] << 8);
    int secondByteValue = 0xff & lengthBytes[1];
    int length = firstByteValue + secondByteValue;

    byte[] raw = bufferedInputStream.readNBytes(length);

    String body = new String(raw, StandardCharsets.UTF_8);

    return SocketMessage.parse(body);
  }

  /**
   * Decodes a web socket encoded message from the client. Note, this is a blocking operation.
   *
   * @param bufferedInputStream the {@link BufferedInputStream} to read the input from
   * @return the decoded message or null if disconnected
   * @throws IOException if an I/O error occurs
   */
  public SocketMessage decodeForWeb(BufferedInputStream bufferedInputStream) throws IOException {
    // first byte is op code and second is length
    byte[] starterBytes = bufferedInputStream.readNBytes(2);

    if (starterBytes.length == 0) {
      return null;
    }

    int length = 0xff & (starterBytes[1] - 0x80);

    if (length > 125) {
      byte[] newLengthBytes = bufferedInputStream.readNBytes(2);
      int firstByteValue = 0xff & (newLengthBytes[0] << 8);
      int secondByteValue = 0xff & newLengthBytes[1];

      length = firstByteValue + secondByteValue;
    }

    byte[] key = bufferedInputStream.readNBytes(4);

    byte[] raw = bufferedInputStream.readNBytes(length);
    byte[] decoded = new byte[length];
    for (int i = 0; i < raw.length; i++) {
      decoded[i] = (byte) (raw[i] ^ key[i & 0x3]);
    }

    String body = new String(decoded, StandardCharsets.UTF_8);

    if (body.isEmpty()) {
      return null;
    }

    return SocketMessage.parse(body);
  }
}
