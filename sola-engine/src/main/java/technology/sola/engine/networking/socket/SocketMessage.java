package technology.sola.engine.networking.socket;

import java.io.Serializable;

public interface SocketMessage<T> extends Serializable {
  T body();
}
