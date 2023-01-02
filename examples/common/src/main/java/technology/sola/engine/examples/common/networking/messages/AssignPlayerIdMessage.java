package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

public class AssignPlayerIdMessage extends SocketMessage {
  private final long clientPlayerId;

  public AssignPlayerIdMessage(long clientPlayerId) {
    super(MessageTypes.ASSIGN_PLAYER_ID.ordinal(), "" + clientPlayerId);
    this.clientPlayerId = clientPlayerId;
  }

  public static AssignPlayerIdMessage fromBody(String body) {
    long clientPlayerId = Long.parseLong(body);

    return new AssignPlayerIdMessage(clientPlayerId);
  }

  public long getClientPlayerId() {
    return clientPlayerId;
  }
}
