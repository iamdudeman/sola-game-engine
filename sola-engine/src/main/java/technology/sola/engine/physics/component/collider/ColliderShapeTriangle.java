package technology.sola.engine.physics.component.collider;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

/**
 * ColliderShapeTriangle is a {@link ColliderShape} implementation for a triangle which utilize a
 * {@link Triangle} for its geometric shape representation.
 *
 * @param shape the triangle shape of this collider
 */
public record ColliderShapeTriangle(
  Triangle shape
) implements ColliderShape<Triangle> {
  /**
   * Creates a "unit triangle" collider shape with uniform side lengths of 1.
   */
  public ColliderShapeTriangle() {
    this(new Triangle(new Vector2D(0, 0), new Vector2D(0.5f, 1), new Vector2D(1, 0)));
  }

  @Override
  public ColliderType type() {
    return ColliderType.TRIANGLE;
  }

  @Override
  public float getBoundingWidth(float transformScaleX) {
    float minX = shape.p1().x();
    float maxX = minX;

    minX = Math.min(minX, shape.p2().x());
    maxX = Math.max(maxX, shape.p2().x());

    minX = Math.min(minX, shape.p3().x());
    maxX = Math.max(maxX, shape.p3().x());

    return (maxX - minX) * transformScaleX;
  }

  @Override
  public float getBoundingHeight(float transformScaleY) {
    float minY = shape.p1().y();
    float maxY = minY;

    minY = Math.min(minY, shape.p2().y());
    maxY = Math.max(maxY, shape.p2().y());

    minY = Math.min(minY, shape.p3().y());
    maxY = Math.max(maxY, shape.p3().y());

    return (maxY - minY) * transformScaleY;
  }

  @Override
  public Triangle getShape(TransformComponent transformComponent, float offsetX, float offsetY) {
    var matrix = Matrix3D.translate(transformComponent.getX() + offsetX, transformComponent.getY() + offsetY)
      .multiply(Matrix3D.scale(transformComponent.getScaleX(), transformComponent.getScaleY()));

    return new Triangle(
      matrix.multiply(shape.p1()),
      matrix.multiply(shape.p2()),
      matrix.multiply(shape.p3())
    );
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
