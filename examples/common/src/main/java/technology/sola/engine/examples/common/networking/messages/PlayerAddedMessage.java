package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

public class PlayerAddedMessage extends SocketMessage {
  private final long clientPlayerId;

  public PlayerAddedMessage(long clientPlayerId) {
    super(MessageTypes.PLAYER_ADDED.ordinal(), "" + clientPlayerId);
    this.clientPlayerId = clientPlayerId;
  }

  public static PlayerAddedMessage fromBody(String body) {
    long clientPlayerId = Long.parseLong(body);

    return new PlayerAddedMessage(clientPlayerId);
  }

  public long getClientPlayerId() {
    return clientPlayerId;
  }
}
