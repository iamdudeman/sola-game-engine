package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;

public record PlayerRemovedMessage(long id) implements SocketMessage {
}
