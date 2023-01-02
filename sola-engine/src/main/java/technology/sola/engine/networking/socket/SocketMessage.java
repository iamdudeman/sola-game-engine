package technology.sola.engine.networking.socket;

public class SocketMessage {
  private final int type;
  private final String body;

  public SocketMessage(int type, String body) {
    if (body.getBytes().length > 8192 - 4) {
      throw new IllegalArgumentException("Message body can only be 8188 bytes long");
    }

    this.type = type;
    this.body = body;
  }

  public static SocketMessage fromString(String message) {
    String[] parts = message.split("-");
    int type = Integer.parseInt(parts[0]);
    String body = parts.length == 2 ? parts[1] : "";

    return new SocketMessage(type, body);
  }

  public int getType() {
    return type;
  }

  public String getBody() {
    return body;
  }

  @Override
  public String toString() {
    return type + "-" + body + "\n";
  }
}
