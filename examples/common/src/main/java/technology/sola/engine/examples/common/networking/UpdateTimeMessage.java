package technology.sola.engine.examples.common.networking;

import technology.sola.engine.networking.socket.SocketMessage;

public record UpdateTimeMessage(long time) implements SocketMessage {
}
