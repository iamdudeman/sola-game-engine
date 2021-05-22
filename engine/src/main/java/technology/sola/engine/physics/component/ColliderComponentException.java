package technology.sola.engine.physics.component;

public class ColliderComponentException extends RuntimeException {
  ColliderComponentException(ColliderComponent.ColliderType actual, ColliderComponent.ColliderType required) {
    super(String.format("The ColliderComponent could not be used as type [%s], actual type [%s]", required, actual));
  }
}
