package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

public class PlayerRemovedMessage extends SocketMessage {
  private final long clientPlayerId;

  public PlayerRemovedMessage(long clientPlayerId) {
    super(MessageType.PLAYER_REMOVED.ordinal(), "" + clientPlayerId);
    this.clientPlayerId = clientPlayerId;
  }

  public static PlayerRemovedMessage parse(SocketMessage socketMessage) {
    long clientPlayerId = Long.parseLong(socketMessage.getBody());

    return new PlayerRemovedMessage(clientPlayerId);
  }

  public long getClientPlayerId() {
    return clientPlayerId;
  }
}
