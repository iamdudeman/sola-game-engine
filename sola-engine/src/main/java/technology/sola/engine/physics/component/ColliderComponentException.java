package technology.sola.engine.physics.component;

import java.io.Serial;

class ColliderComponentException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 7023803331836501667L;

  ColliderComponentException(ColliderComponent.ColliderType actual, ColliderComponent.ColliderType required) {
    super(String.format("The ColliderComponent could not be used as type [%s], actual type [%s]", required, actual));
  }
}
