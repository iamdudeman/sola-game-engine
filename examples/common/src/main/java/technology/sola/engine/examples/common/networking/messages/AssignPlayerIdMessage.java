package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

/**
 * {@link SocketMessage} for assigning a player an id.
 */
public class AssignPlayerIdMessage extends SocketMessage {
  private final long clientPlayerId;

  /**
   * Creates a new {@link AssignPlayerIdMessage} with the player's id.
   *
   * @param clientPlayerId the player's id
   */
  public AssignPlayerIdMessage(long clientPlayerId) {
    super(MessageType.ASSIGN_PLAYER_ID.ordinal(), String.valueOf(clientPlayerId));
    this.clientPlayerId = clientPlayerId;
  }

  /**
   * Parses a {@link SocketMessage} as a {@link AssignPlayerIdMessage}.
   *
   * @param socketMessage the message to parse
   * @return the parsed AssignPlayerIdMessage
   */
  public static AssignPlayerIdMessage parse(SocketMessage socketMessage) {
    long clientPlayerId = Long.parseLong(socketMessage.getBody());

    return new AssignPlayerIdMessage(clientPlayerId);
  }

  /**
   * @return the player's id
   */
  public long getClientPlayerId() {
    return clientPlayerId;
  }
}
