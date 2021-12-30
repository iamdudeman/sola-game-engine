package technology.sola.engine.ecs;

import java.io.Serializable;

public interface Component<T extends Component> extends Serializable {
  T copy();
}
