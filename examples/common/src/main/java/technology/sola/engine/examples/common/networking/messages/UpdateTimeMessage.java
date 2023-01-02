package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

public class UpdateTimeMessage extends SocketMessage {
  public UpdateTimeMessage() {
    super(MessageTypes.UPDATE_TIME.ordinal(), "" + System.currentTimeMillis());
  }

  public String getTime() {
    return getBody();
  }
}
