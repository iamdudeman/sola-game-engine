package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.math.linear.Vector2D;

public class PlayerUpdateMessage extends SocketMessage {
  private final long clientPlayerId;
  private final Vector2D position;

  public PlayerUpdateMessage(long clientPlayerId, Vector2D position) {
    super(MessageType.PLAYER_UPDATE.ordinal(), clientPlayerId + "_" + position.x() + "_" + position.y());
    this.clientPlayerId = clientPlayerId;
    this.position = position;
  }

  public static PlayerUpdateMessage parse(SocketMessage socketMessage) {
    String[] parts = socketMessage.getBody().split("_");
    long clientPlayerId = Long.parseLong(parts[0]);
    float x = Float.parseFloat(parts[1]);
    float y = Float.parseFloat(parts[2]);

    return new PlayerUpdateMessage(clientPlayerId, new Vector2D(x, y));
  }

  public long getClientPlayerId() {
    return clientPlayerId;
  }

  public Vector2D getPosition() {
    return position;
  }
}
