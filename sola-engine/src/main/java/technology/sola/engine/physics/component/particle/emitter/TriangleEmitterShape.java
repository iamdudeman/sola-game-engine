package technology.sola.engine.physics.component.particle.emitter;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

@NullMarked
public class TriangleEmitterShape extends ParticleEmitterShape {
  private boolean isEmitFromBase = true;
  private Triangle triangle;
  private Vector2D center;

  public TriangleEmitterShape(Vector2D direction, float height, float width) {
    Vector2D p1 = new Vector2D(0, 0);
    Vector2D pL = p1.add(direction.normalize().scalar(height));
    Vector2D perpPL = new Vector2D(pL.y(), -pL.x()).normalize();
    Vector2D p2 = pL.add(perpPL.scalar(width * 0.5f));
    Vector2D p3 = pL.subtract(perpPL.scalar(width * 0.5f));

    triangle = new Triangle(p1, p2, p3);
    center = triangle.getCentroid();
  }

  @Override
  public EmissionDetails nextEmission() {
    if (isEmitFromShell()) {
      return super.nextEmission();
    }

    if (isEmitFromBase) {
      var sideLength = triangle.p2().distance(triangle.p3());
      var direction = triangle.p3().subtract(triangle.p2()).normalize();
      var point = triangle.p2().add(direction.scalar(SolaRandom.nextFloat() * sideLength));

      return new EmissionDetails(
        new Vector2D(0, 0),
        point.normalize()
      );
    }

    return super.nextEmission();
  }

  public TriangleEmitterShape setEmitFromBase(boolean emitFromBase) {
    this.isEmitFromBase = emitFromBase;

    return this;
  }

  @Override
  protected Vector2D getCenter() {
    return center;
  }

  @Override
  protected Vector2D randomPointInShape() {
    float x = SolaRandom.nextFloat();
    float y = SolaRandom.nextFloat();

    float q = Math.abs(x - y);

    float s = q;
    float t = 0.5f * (x + y - q);
    float u = 1 - (0.5f * (x + y + q));

    return new Vector2D(
      s * triangle.p1().x() + t * triangle.p2().x() + u * triangle.p3().x(),
      s * triangle.p1().y() + t * triangle.p2().y() + u * triangle.p3().y()
    );
  }

  @Override
  protected Vector2D randomPointOnPerimeter() {
    int side = SolaRandom.nextInt(3);

    if (side == 0) {
      var sideLength = triangle.p1().distance(triangle.p2());
      var direction = triangle.p2().subtract(triangle.p1()).normalize();

      return triangle.p1().add(direction.scalar(SolaRandom.nextFloat() * sideLength));
    } else if (side == 1) {
      var sideLength = triangle.p2().distance(triangle.p3());
      var direction = triangle.p3().subtract(triangle.p2()).normalize();

      return triangle.p2().add(direction.scalar(SolaRandom.nextFloat() * sideLength));
    }

    var sideLength = triangle.p1().distance(triangle.p3());
    var direction = triangle.p3().subtract(triangle.p1()).normalize();

    return triangle.p1().add(direction.scalar(SolaRandom.nextFloat() * sideLength));
  }
}
