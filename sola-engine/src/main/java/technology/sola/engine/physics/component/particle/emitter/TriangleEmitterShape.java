package technology.sola.engine.physics.component.particle.emitter;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

/**
 * TriangleEmitterShape is a {@link ParticleEmitterShape} that emits particles in the shape of an Isoceles triangle.
 */
@NullMarked
public class TriangleEmitterShape extends ParticleEmitterShape {
  private final Triangle triangle;
  private final Vector2D center;
  private boolean isEmitFromBase = true;

  /**
   * Creates a TriangleEmitterShape. The direction is the vector going from the isolated point of the triangle towards
   * the non-equal side. The height is the distance the non-equal side is away from the isolated point, and the width
   * is the width of the non-equal side.
   * <pre>
   * Ex: direction (0, -1) + height 3 + width 5
   * ______
   * \    /
   *  \  /
   *   \/
   * </pre>
   *
   * @param direction the direction of the triangle
   * @param height    the height of the triangle
   * @param width     the width of the triangle
   */
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

  /**
   * Sets whether particles should be emitted from the base of the triangle (the isolated point).
   *
   * @param emitFromBase whether particles should be emitted from the base of the triangle
   * @return this
   */
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

    float difference = Math.abs(x - y);

    float w1 = difference;
    float w2 = 0.5f * (x + y - difference);
    float w3 = 1 - (0.5f * (x + y + difference));
    var triangle = this.triangle;

    return new Vector2D(
      w1 * triangle.p1().x() + w2 * triangle.p2().x() + w3 * triangle.p3().x(),
      w1 * triangle.p1().y() + w2 * triangle.p2().y() + w3 * triangle.p3().y()
    );
  }

  @Override
  protected Vector2D randomPointOnPerimeter() {
    int side = SolaRandom.nextInt(3);
    var triangle = this.triangle;

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
