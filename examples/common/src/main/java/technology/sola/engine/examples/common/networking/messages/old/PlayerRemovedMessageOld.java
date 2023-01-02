package technology.sola.engine.examples.common.networking.messages.old;

import technology.sola.engine.networking.socket.SocketMessageOld;

public record PlayerRemovedMessageOld(long id) implements SocketMessageOld {
}
