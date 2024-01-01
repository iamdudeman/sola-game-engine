package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.math.linear.Vector2D;

/**
 * {@link SocketMessage} for notifying that a player's position was updated.
 */
public class PlayerUpdateMessage extends SocketMessage {
  private final long clientPlayerId;
  private final Vector2D position;

  /**
   * Creates a new {@link PlayerUpdateMessage} with the updated player's id and new position.
   *
   * @param clientPlayerId the updated player's id
   * @param position       the player's position
   */
  public PlayerUpdateMessage(long clientPlayerId, Vector2D position) {
    super(MessageType.PLAYER_UPDATE.ordinal(), clientPlayerId + "_" + position.x() + "_" + position.y());
    this.clientPlayerId = clientPlayerId;
    this.position = position;
  }

  /**
   * Parses a {@link SocketMessage} as a {@link PlayerUpdateMessage}.
   *
   * @param socketMessage the message to parse
   * @return the parsed PlayerUpdateMessage
   */
  public static PlayerUpdateMessage parse(SocketMessage socketMessage) {
    String[] parts = socketMessage.getBody().split("_");
    long clientPlayerId = Long.parseLong(parts[0]);
    float x = Float.parseFloat(parts[1]);
    float y = Float.parseFloat(parts[2]);

    return new PlayerUpdateMessage(clientPlayerId, new Vector2D(x, y));
  }

  /**
   * @return the updated player's id
   */
  public long getClientPlayerId() {
    return clientPlayerId;
  }

  /**
   * @return the new position of the player
   */
  public Vector2D getPosition() {
    return position;
  }
}
