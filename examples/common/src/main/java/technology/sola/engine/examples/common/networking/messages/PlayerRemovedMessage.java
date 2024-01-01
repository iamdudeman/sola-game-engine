package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

/**
 * {@link SocketMessage} for notifying that a player was removed.
 */
public class PlayerRemovedMessage extends SocketMessage {
  private final long clientPlayerId;

  /**
   * Creates a new {@link PlayerRemovedMessage} with the removed player's id.
   *
   * @param clientPlayerId the removed player's id
   */
  public PlayerRemovedMessage(long clientPlayerId) {
    super(MessageType.PLAYER_REMOVED.ordinal(), String.valueOf(clientPlayerId));
    this.clientPlayerId = clientPlayerId;
  }

  /**
   * Parses a {@link SocketMessage} as a {@link PlayerRemovedMessage}.
   *
   * @param socketMessage the message to parse
   * @return the parsed PlayerRemovedMessage
   */
  public static PlayerRemovedMessage parse(SocketMessage socketMessage) {
    long clientPlayerId = Long.parseLong(socketMessage.getBody());

    return new PlayerRemovedMessage(clientPlayerId);
  }

  /**
   * @return the removed player's id
   */
  public long getClientPlayerId() {
    return clientPlayerId;
  }
}
