package technology.sola.engine.examples.common.networking.messages.old;

import technology.sola.engine.networking.socket.SocketMessageOld;
import technology.sola.math.linear.Vector2D;

public record PlayerUpdateMessageOld(long id, Vector2D position) implements SocketMessageOld {
}
