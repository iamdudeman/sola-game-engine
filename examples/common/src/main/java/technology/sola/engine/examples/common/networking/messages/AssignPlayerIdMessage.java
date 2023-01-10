package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

public class AssignPlayerIdMessage extends SocketMessage {
  private final long clientPlayerId;

  public AssignPlayerIdMessage(long clientPlayerId) {
    super(MessageType.ASSIGN_PLAYER_ID.ordinal(), "" + clientPlayerId);
    this.clientPlayerId = clientPlayerId;
  }

  public static AssignPlayerIdMessage parse(SocketMessage socketMessage) {
    long clientPlayerId = Long.parseLong(socketMessage.getBody());

    return new AssignPlayerIdMessage(clientPlayerId);
  }

  public long getClientPlayerId() {
    return clientPlayerId;
  }
}
