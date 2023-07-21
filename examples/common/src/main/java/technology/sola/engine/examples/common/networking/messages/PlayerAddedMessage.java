package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

/**
 * {@link SocketMessage} for notifying that a player was added.
 */
public class PlayerAddedMessage extends SocketMessage {
  private final long clientPlayerId;

  /**
   * Creates a new {@link PlayerAddedMessage} with the newly added player's id.
   *
   * @param clientPlayerId the added player's id
   */
  public PlayerAddedMessage(long clientPlayerId) {
    super(MessageType.PLAYER_ADDED.ordinal(), String.valueOf(clientPlayerId));
    this.clientPlayerId = clientPlayerId;
  }

  /**
   * Parses a {@link SocketMessage} as a {@link PlayerAddedMessage}.
   *
   * @param socketMessage the message to parse
   * @return the parsed PlayerAddedMessage
   */
  public static PlayerAddedMessage parse(SocketMessage socketMessage) {
    long clientPlayerId = Long.parseLong(socketMessage.getBody());

    return new PlayerAddedMessage(clientPlayerId);
  }

  /**
   * @return the added player's id
   */
  public long getClientPlayerId() {
    return clientPlayerId;
  }
}
