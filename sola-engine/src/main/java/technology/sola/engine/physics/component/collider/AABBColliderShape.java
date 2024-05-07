package technology.sola.engine.physics.component.collider;

public record AABBColliderShape(float width, float height) implements ColliderShape {
  @Override
  public ColliderType type() {
    return ColliderType.AABB;
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
