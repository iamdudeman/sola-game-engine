package technology.sola.engine.ecs;

import java.io.Serializable;

public interface Component<T> extends Serializable {
  T copy();
}
