package technology.sola.engine.networking.socket;

public class SocketMessage {
  private static final String SEPARATOR = "~";
  private final int type;
  private final String body;

  public SocketMessage(int type, String body) {
    this.type = type;
    this.body = body;

    // 65535 is the max size minus 2 bytes for type and separator character
    if (body.getBytes().length > 65535 - 2) {
      throw new IllegalArgumentException("Message body can only be 65533 bytes long");
    }
  }

  public static SocketMessage fromString(String message) {
    String[] parts = message.split(SEPARATOR);
    int type = Integer.parseInt(parts[0]);
    String body = parts.length == 2 ? parts[1] : "";

    return new SocketMessage(type, body.trim());
  }

  public int getType() {
    return type;
  }

  public String getBody() {
    return body;
  }

  @Override
  public String toString() {
    return type + SEPARATOR + body;
  }
}
