package technology.sola.engine.examples.common.networking;

import technology.sola.engine.networking.socket.SocketMessage;

public record ChatSocketMessage(String username, String text) implements SocketMessage {
  @Override
  public String toString() {
    return username + " - " + text;
  }
}
