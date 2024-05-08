package technology.sola.engine.physics.component.collider;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

public record ColliderShapeAABB(
  float width,
  float height
) implements ColliderShape<Rectangle> {
  public ColliderShapeAABB() {
    this(1, 1);
  }

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

  @Override
  public Rectangle getShape(TransformComponent transformComponent, float offsetX, float offsetY) {
    Vector2D min = new Vector2D(
      transformComponent.getX() + offsetX,
      transformComponent.getY() + offsetY
    );
    Vector2D max = new Vector2D(
      min.x() + transformComponent.getScaleX() * width,
      min.y() + transformComponent.getScaleY() * height
    );

    return new Rectangle(min, max);
  }

  @Override
  public void debugRender(Renderer renderer, TransformComponent transformComponent, float offsetX, float offsetY) {
    Rectangle rectangle = getShape(transformComponent, offsetX, offsetY);

    renderer.drawRect(rectangle.min().x(), rectangle.min().y(), rectangle.getWidth(), rectangle.getHeight(), Color.RED);
  }
}
