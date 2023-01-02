package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

public class UpdateTimeMessage extends SocketMessage {
  private final long time;

  public UpdateTimeMessage(long time) {
    super(MessageTypes.UPDATE_TIME.ordinal(), "" + time);
    this.time = time;
  }

  public static UpdateTimeMessage fromBody(String body) {
    long time = Long.parseLong(body);

    return new UpdateTimeMessage(time);
  }

  public long getTime() {
    return time;
  }
}
