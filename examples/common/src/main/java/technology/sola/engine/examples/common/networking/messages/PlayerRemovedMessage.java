package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

public class PlayerRemovedMessage extends SocketMessage {
  private final long clientPlayerId;

  public PlayerRemovedMessage(long clientPlayerId) {
    super(MessageTypes.PLAYER_REMOVED.ordinal(), "" + clientPlayerId);
    this.clientPlayerId = clientPlayerId;
  }

  public static PlayerRemovedMessage fromBody(String body) {
    long clientPlayerId = Long.parseLong(body);

    return new PlayerRemovedMessage(clientPlayerId);
  }

  public long getClientPlayerId() {
    return clientPlayerId;
  }
}
