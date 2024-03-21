package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

/**
 * PlayerMoveMessage is a {@link SocketMessage} for telling the server where the player desired to move.
 */
public class PlayerMoveMessage extends SocketMessage {
  private final int direction;

  /**
   * Creates a {@link SocketMessage} telling the server where the player wants to move.
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

  /**
   * The direction the player is moving in.
   *
   * @return STOP - 0, LEFT - 1, RIGHT - 2
   */
  public int getDirection() {
    return direction;
  }
}
