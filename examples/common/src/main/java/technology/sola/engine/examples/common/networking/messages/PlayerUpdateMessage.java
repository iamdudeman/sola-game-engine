package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.math.linear.Vector2D;

public record PlayerUpdateMessage(long id, Vector2D position) implements SocketMessage {
}
