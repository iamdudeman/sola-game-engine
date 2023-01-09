package technology.sola.engine.networking.socket;

/**
 * SocketMessage contains data transmitted between a server and client.
 */
public class SocketMessage {
  private static final String SEPARATOR = "~";
  private final int type;
  private final String body;

  /**
   * Creates a new SocketMessage instance. Body must be 65533 bytes or fewer.
   *
   * @param type the type of the message
   * @param body the body of the message
   */
  public SocketMessage(int type, String body) {
    this.type = type;
    this.body = body;

    // 65535 is the max size minus 2 bytes for type and separator character
    if (body.getBytes().length > 65535 - 2) {
      throw new IllegalArgumentException("Message body can only be 65533 bytes long");
    }
  }

  /**
   * Creates a SocketMessage instance from a raw string message.
   *
   * @param message the raw message
   * @return the parsed {@link SocketMessage} instance
   */
  public static SocketMessage parse(String message) {
    String[] parts = message.split(SEPARATOR);
    int type = Integer.parseInt(parts[0]);
    String body = parts.length == 2 ? parts[1] : "";

    return new SocketMessage(type, body);
  }

  /**
   * @return the type of the message
   */
  public int getType() {
    return type;
  }

  /**
   * @return the body of the message
   */
  public String getBody() {
    return body;
  }

  @Override
  public String toString() {
    return type + SEPARATOR + body;
  }
}
