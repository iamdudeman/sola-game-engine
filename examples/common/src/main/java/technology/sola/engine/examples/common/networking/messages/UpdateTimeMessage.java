package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

public class UpdateTimeMessage extends SocketMessage {
  private final long time;

  public UpdateTimeMessage(long time) {
    super(MessageType.UPDATE_TIME.ordinal(), "" + time);
    this.time = time;
  }

  public static UpdateTimeMessage parse(SocketMessage socketMessage) {
    long time = Long.parseLong(socketMessage.getBody());

    return new UpdateTimeMessage(time);
  }

  public long getTime() {
    return time;
  }
}
