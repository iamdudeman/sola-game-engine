package technology.sola.engine.physics.component.collider;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Entity;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.geometry.*;
import technology.sola.math.linear.Vector2D;

/**
 * ColliderShapeCircle is a {@link ColliderShape} implementation for a circle which utilize a
 * {@link Circle} for its geometric shape representation. If scaleX and scaleY are not equal then an {@link Ellipse}
 * will be used for the shape.
 *
 * @param radius the radius of the circle
 */
@NullMarked
public record ColliderShapeCircle(
  float radius
) implements ColliderShape<Shape> {
  /**
   * Creates an instance with radius set to 0.5f.
   */
  public ColliderShapeCircle() {
    this(0.5f);
  }

  @Override
  public Rectangle getBoundingBox(Entity entity, TransformComponent transformComponent, float offsetX, float offsetY) {
    if (isCircle(transformComponent)) {
      var min = transformComponent.getTranslate().add(new Vector2D(offsetX, offsetY));
      float sideLength = radius * 2 * transformComponent.getScaleX();

      return new Rectangle(
        min,
        min.add(new Vector2D(sideLength, sideLength))
      );
    }

    var min = transformComponent.getTranslate().add(new Vector2D(offsetX, offsetY));
    float width = radius * 2 * transformComponent.getScaleX();
    float height = radius * 2 * transformComponent.getScaleY();

    return new Rectangle(
      min,
      min.add(new Vector2D(width, height))
    );
  }

  @Override
  public Shape getShape(Entity entity, TransformComponent transformComponent, float offsetX, float offsetY) {
    if (isCircle(transformComponent)) {
      float transformScale = transformComponent.getScaleX();
      float radiusWithTransform = radius * transformScale;

      Vector2D center = new Vector2D(
        transformComponent.getX() + radiusWithTransform + offsetX,
        transformComponent.getY() + radiusWithTransform + offsetY
      );

      return new Circle(radiusWithTransform, center);
    }

    float radiusX = radius * transformComponent.getScaleX();
    float radiusY = radius * transformComponent.getScaleY();
    Vector2D center = new Vector2D(
      transformComponent.getX() + radiusX + offsetX,
      transformComponent.getY() + radiusY + offsetY
    );

    return new Ellipse(center, radiusX, radiusY);
  }

  @Override
  public void debugRender(Renderer renderer, Entity entity, TransformComponent transformComponent, float offsetX, float offsetY) {
    if (isCircle(transformComponent)) {
      Circle circle = (Circle) getShape(entity, transformComponent, offsetX, offsetY);

      renderer.drawCircle(circle.center().x() - circle.radius(), circle.center().y() - circle.radius(), circle.radius(), Color.RED);
    } else {
      Ellipse shape = (Ellipse) getShape(entity, transformComponent, offsetX, offsetY);

      renderer.drawPolygon(shape.points(), Color.RED);
    }
  }

  private boolean isCircle(TransformComponent transformComponent) {
    return transformComponent.getScaleX() == transformComponent.getScaleY();
  }
}
