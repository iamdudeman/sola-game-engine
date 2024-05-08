package technology.sola.engine.physics.component.collider;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.geometry.Circle;
import technology.sola.math.linear.Vector2D;

public record ColliderShapeCircle(
  float radius
) implements ColliderShape<Circle> {
  public ColliderShapeCircle() {
    this(0.5f);
  }

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

  @Override
  public Circle getShape(TransformComponent transformComponent, float offsetX, float offsetY) {
    float transformScale = Math.max(transformComponent.getScaleX(), transformComponent.getScaleY());
    float radiusWithTransform = radius * transformScale;

    Vector2D center = new Vector2D(
      transformComponent.getX() + radiusWithTransform + offsetX,
      transformComponent.getY() + radiusWithTransform + offsetY
    );

    return new Circle(radiusWithTransform, center);
  }

  @Override
  public void debugRender(Renderer renderer, TransformComponent transformComponent, float offsetX, float offsetY) {
    Circle circle = getShape(transformComponent, offsetX, offsetY);

    renderer.drawCircle(circle.center().x() - circle.radius(), circle.center().y() - circle.radius(), circle.radius(), Color.RED);
  }
}
