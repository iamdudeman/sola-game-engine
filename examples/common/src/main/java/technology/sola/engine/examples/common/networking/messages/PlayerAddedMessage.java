package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

public class PlayerAddedMessage extends SocketMessage {
  private final long clientPlayerId;

  public PlayerAddedMessage(long clientPlayerId) {
    super(MessageType.PLAYER_ADDED.ordinal(), "" + clientPlayerId);
    this.clientPlayerId = clientPlayerId;
  }

  public static PlayerAddedMessage parse(SocketMessage socketMessage) {
    long clientPlayerId = Long.parseLong(socketMessage.getBody());

    return new PlayerAddedMessage(clientPlayerId);
  }

  public long getClientPlayerId() {
    return clientPlayerId;
  }
}
