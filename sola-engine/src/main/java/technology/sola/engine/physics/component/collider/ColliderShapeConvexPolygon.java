package technology.sola.engine.physics.component.collider;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.geometry.ConvexPolygon;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

/**
 * ColliderShapeConvexPolygon is a {@link ColliderShape} implementation for a convex polygon which utilize a
 * {@link ConvexPolygon} for its geometric shape representation.
 *
 * @param shape the convex polygon shape of this collider
 */
@NullMarked
public record ColliderShapeConvexPolygon(
  ConvexPolygon shape
) implements ColliderShape<ConvexPolygon> {
  @Override
  public ColliderType type() {
    return ColliderType.CONVEX_POLYGON;
  }

  @Override
  public Rectangle getBoundingBox(TransformComponent transformComponent, float offsetX, float offsetY) {
    var shape = getShape(transformComponent, offsetX, offsetY);

    return shape.boundingBox();
  }

  @Override
  public ConvexPolygon getShape(TransformComponent transformComponent, float offsetX, float offsetY) {
    var matrix = Matrix3D.translate(transformComponent.getX() + offsetX, transformComponent.getY() + offsetY)
      .multiply(Matrix3D.scale(transformComponent.getScaleX(), transformComponent.getScaleY()));
    var transformedPoints = new Vector2D[shape.points().length];

    for (int i = 0; i < shape.points().length; i++) {
      transformedPoints[i] = matrix.multiply(shape.points()[i]);
    }

    return new ConvexPolygon(transformedPoints);
  }

  @Override
  public void debugRender(Renderer renderer, TransformComponent transformComponent, float offsetX, float offsetY) {
    ConvexPolygon shape = getShape(transformComponent, offsetX, offsetY);

    renderer.drawPolygon(shape.points(), Color.RED);
  }
}
