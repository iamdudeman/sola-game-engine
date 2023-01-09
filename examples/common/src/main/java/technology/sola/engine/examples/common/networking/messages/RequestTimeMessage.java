package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

public class RequestTimeMessage extends SocketMessage {
  public RequestTimeMessage() {
    super(MessageType.REQUEST_TIME.ordinal(), "");
  }
}
