package technology.sola.engine.examples.common.networking;

import technology.sola.engine.networking.socket.SocketMessage;

public record ChatSocketMessage(String body) implements SocketMessage<String> {
}
