package technology.sola.engine.physics.component.collider;

public record CircleColliderShape(float radius) implements ColliderShape {
  @Override
  public ColliderType type() {
    return ColliderType.CIRCLE;
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
