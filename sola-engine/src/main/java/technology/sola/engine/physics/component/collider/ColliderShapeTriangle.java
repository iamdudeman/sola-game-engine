package technology.sola.engine.physics.component.collider;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

/**
 * ColliderShapeTriangle is a {@link ColliderShape} implementation for a triangle which utilize a
 * {@link Triangle} for its geometric shape representation. The offset percentage of the vertical point. This modifies
 * where it appears on the horizontal access. 0.5 is midway, 0 and 1 is all the way left or right respectively creating
 * a right triangle.
 *
 * @param width                         the width of the triangle
 * @param height                        the height of the triangle
 * @param verticalPointOffsetPercentage the vertical point offset percentage
 */
public record ColliderShapeTriangle(
  float width,
  float height,
  float verticalPointOffsetPercentage
) implements ColliderShape<Triangle> {
  /**
   * Creates a triangle with width of 1, height of 1 and vertical point offset percentage of 0.5.
   */
  public ColliderShapeTriangle() {
    this(1, 1, 0.5f);
  }

  /**
   * Creates a triangle with desired width and height and vertical point offset percentage of 0.5.
   *
   * @param width  the width of the triangle
   * @param height the height of the triangle
   */
  public ColliderShapeTriangle(float width, float height) {
    this(width, height, 0.5f);
  }

  @Override
  public ColliderType type() {
    return ColliderType.TRIANGLE;
  }

  @Override
  public float getBoundingWidth(float transformScaleX) {
    return width;
  }

  @Override
  public float getBoundingHeight(float transformScaleY) {
    return height;
  }

  @Override
  public Triangle getShape(TransformComponent transformComponent, float offsetX, float offsetY) {
    Vector2D firstPoint = transformComponent.getTranslate();
    Vector2D secondPoint = transformComponent.getTranslate().add(new Vector2D(
      transformComponent.getScaleX(),
      0
    ));
    Vector2D thirdPoint = firstPoint.add(new Vector2D(
      transformComponent.getScaleX() * verticalPointOffsetPercentage,
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
