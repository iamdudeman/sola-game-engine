package technology.sola.engine.physics.component.collider;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

/**
 * ColliderShapeCircle is a {@link ColliderShape} implementation for a circle which utilize a
 * {@link Circle} for its geometric shape representation.
 * <br>
 * Note: only {@link TransformComponent#getScaleX()} is used for size of collider
 *
 * @param radius the radius of the circle
 */
@NullMarked
public record ColliderShapeCircle(
  float radius
) implements ColliderShape<Circle> {
  /**
   * Creates an instance with radius set to 0.5f.
   */
  public ColliderShapeCircle() {
    this(0.5f);
  }

  @Override
  public ColliderType type() {
    return ColliderType.CIRCLE;
  }

  @Override
  public Rectangle getBoundingBox(TransformComponent transformComponent, float offsetX, float offsetY) {
    var min = transformComponent.getTranslate().add(new Vector2D(offsetX, offsetY));
    float sideLength = radius * 2 * transformComponent.getScaleX();

    return new Rectangle(
      min,
      min.add(new Vector2D(sideLength, sideLength))
    );
  }

  @Override
  public Circle getShape(TransformComponent transformComponent, float offsetX, float offsetY) {
    float transformScale = transformComponent.getScaleX();
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
