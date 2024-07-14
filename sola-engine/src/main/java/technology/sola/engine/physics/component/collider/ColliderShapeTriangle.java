package technology.sola.engine.physics.component.collider;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

/**
 * ColliderShapeTriangle is a {@link ColliderShape} implementation for a triangle which utilize a
 * {@link Triangle} for its geometric shape representation.
 *
 * @param width
 * @param height
 * @param verticalPointOffset
 */
public record ColliderShapeTriangle(
  float width,
  float height,
  float verticalPointOffset
) implements ColliderShape<Triangle> {
  public ColliderShapeTriangle() {
    this(1, 1, 0);
  }

  public ColliderShapeTriangle(float width, float height) {
    this(width, height, 0);
  }

  @Override
  public ColliderType type() {
    return ColliderType.TRIANGLE;
  }

  @Override
  public float getBoundingWidth(float transformScaleX) {
    float offsetAdditionalWidth = Math.abs(verticalPointOffset * 2) - width;

    return (width + Math.max(offsetAdditionalWidth, 0)) * transformScaleX;
  }

  @Override
  public float getBoundingHeight(float transformScaleY) {
    return height * transformScaleY;
  }

  @Override
  public Triangle getShape(TransformComponent transformComponent, float offsetX, float offsetY) {
    Vector2D firstPoint = transformComponent.getTranslate();
    Vector2D secondPoint = transformComponent.getTranslate().add(new Vector2D(
      transformComponent.getScaleX(),
      0
    ));
    Vector2D thirdPoint = firstPoint.add(new Vector2D(
      transformComponent.getScaleX() / 2 + verticalPointOffset,
      transformComponent.getScaleY()
    ));

    return new Triangle(firstPoint, secondPoint, thirdPoint);
  }

  @Override
  public void debugRender(Renderer renderer, TransformComponent transformComponent, float offsetX, float offsetY) {
    Triangle triangle = getShape(transformComponent, offsetX, offsetY);

    renderer.drawTriangle(
      triangle.p1().x(), triangle.p1().y(),
      triangle.p2().x(), triangle.p2().y(),
      triangle.p3().x(), triangle.p3().y(),
      Color.RED
    );
  }
}
