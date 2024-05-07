package technology.sola.engine.physics.component;

import technology.sola.engine.physics.component.collider.ColliderType;

import java.io.Serial;

class ColliderComponentException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 7023803331836501667L;

  ColliderComponentException(ColliderType actual, ColliderType required) {
    super(String.format("The ColliderComponent could not be used as type [%s], actual type [%s]", required, actual));
  }
}
