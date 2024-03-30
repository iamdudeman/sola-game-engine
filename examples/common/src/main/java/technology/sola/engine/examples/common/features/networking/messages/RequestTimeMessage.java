package technology.sola.engine.examples.common.features.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

/**
 * {@link SocketMessage} for requesting the updated time.
 */
public class RequestTimeMessage extends SocketMessage {
  /**
   * Creates a new {@link RequestTimeMessage}.
   */
  public RequestTimeMessage() {
    super(MessageType.REQUEST_TIME.ordinal(), "");
  }
}
