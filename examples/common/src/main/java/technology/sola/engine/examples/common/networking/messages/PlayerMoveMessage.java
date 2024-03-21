package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.math.linear.Vector2D;

public class PlayerMoveMessage extends SocketMessage {
  private final int direction;

  /**
   *
   * @param direction STOP - 0, LEFT - 1, RIGHT - 2
   */
  public PlayerMoveMessage(int direction) {
    super(MessageType.PLAYER_MOVE.ordinal(), String.valueOf(direction));
    this.direction = direction;
  }

  /**
   * Parses a {@link SocketMessage} as a {@link PlayerMoveMessage}.
   *
   * @param socketMessage the message to parse
   * @return the parsed PlayerMoveMessage
   */
  public static PlayerMoveMessage parse(SocketMessage socketMessage) {
    return new PlayerMoveMessage(Integer.parseInt(socketMessage.getBody()));
  }

  public int getDirection() {
    return direction;
  }
}
