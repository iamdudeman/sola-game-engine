package technology.sola.engine.examples.common.features.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

/**
 * {@link SocketMessage} for sending the updated time.
 */
public class UpdateTimeMessage extends SocketMessage {
  private final long time;

  /**
   * Creates a new {@link UpdateTimeMessage} with the current time.
   *
   * @param time the current time
   */
  public UpdateTimeMessage(long time) {
    super(MessageType.UPDATE_TIME.ordinal(), String.valueOf(time));
    this.time = time;
  }

  /**
   * Parses a {@link SocketMessage} as a {@link UpdateTimeMessage}.
   *
   * @param socketMessage the message to parse
   * @return the parsed UpdateTimeMessage
   */
  public static UpdateTimeMessage parse(SocketMessage socketMessage) {
    long time = Long.parseLong(socketMessage.getBody());

    return new UpdateTimeMessage(time);
  }

  /**
   * @return the current time
   */
  public long getTime() {
    return time;
  }
}
