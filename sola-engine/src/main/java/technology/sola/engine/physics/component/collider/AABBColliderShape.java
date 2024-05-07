package technology.sola.engine.physics.component.collider;

import technology.sola.engine.physics.component.ColliderComponent;

public record AABBColliderShape(float width, float height) implements ColliderShape {
  @Override
  public ColliderComponent.ColliderType type() {
    return ColliderComponent.ColliderType.AABB;
  }

  @Override
  public float getBoundingWidth(float transformScaleX) {
    return width * transformScaleX;
  }

  @Override
  public float getBoundingHeight(float transformScaleY) {
    return height * transformScaleY;
  }
}
