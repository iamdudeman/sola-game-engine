package technology.sola.engine.physics.component.collider;

import technology.sola.engine.physics.component.ColliderComponent;

public record CircleColliderShape(float radius) implements ColliderShape {
  @Override
  public ColliderComponent.ColliderType type() {
    return ColliderComponent.ColliderType.CIRCLE;
  }

  @Override
  public float getBoundingWidth(float transformScaleX) {
    return radius * 2 * transformScaleX;
  }

  @Override
  public float getBoundingHeight(float transformScaleY) {
    return radius * 2 * transformScaleY;
  }
}
