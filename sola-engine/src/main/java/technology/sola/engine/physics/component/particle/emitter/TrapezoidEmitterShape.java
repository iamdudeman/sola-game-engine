package technology.sola.engine.physics.component.particle.emitter;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.geometry.ConvexPolygon;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

/**
 * TrapezoidEmitterShape is a {@link ParticleEmitterShape} that emits particles in the shape of a trapezoid.
 */
@NullMarked
public class TrapezoidEmitterShape extends ParticleEmitterShape {
  private final ConvexPolygon trapezoid;
  private final Vector2D center;
  private final Rectangle boundingBox;
  private boolean isEmitFromBase = true;

  /**
   * Creates a TrapezoidEmitterShape. The direction is the vector going from the first base of the trapezoid towards
   * the other base. The height is the distance between the two bases.
   * <pre>
   * Ex: direction (0, -1) + baseWidth 2 + height 5 + otherBaseWidth 8
   * ________
   * \      /
   *  \    /
   *   \__/
   * </pre>
   *
   * @param direction      the direction of the trapezoid
   * @param baseWidth      the width of the first base of the trapezoid
   * @param height         the height of the trapezoid
   * @param otherBaseWidth the width of the second base of the trapezoid
   */
  public TrapezoidEmitterShape(Vector2D direction, float baseWidth, float height, float otherBaseWidth) {
    Vector2D p1 = new Vector2D(0, 0);
    Vector2D pL = p1.add(direction.normalize().scalar(height));
    Vector2D perpPL = new Vector2D(pL.y(), -pL.x()).normalize();
    Vector2D p2 = p1.add(perpPL.scalar(baseWidth));

    pL = pL.add(p2.subtract(p1).scalar(0.5f));

    Vector2D p3 = pL.add(perpPL.scalar(otherBaseWidth));
    Vector2D p4 = pL.subtract(perpPL.scalar(otherBaseWidth));

    trapezoid = new ConvexPolygon(new Vector2D[]{p1, p2, p3, p4});
    center = trapezoid.getCentroid();
    boundingBox = trapezoid.boundingBox();
  }

  @Override
  public EmissionDetails nextEmission() {
    if (isEmitFromShell()) {
      return super.nextEmission();
    }

    if (isEmitFromBase) {
      var points = this.trapezoid.points();
      // random location to move towards the other base
      var sideLength = points[2].distance(points[3]);
      var direction = points[3].subtract(points[2]).normalize();
      var velocity = points[2].add(direction.scalar(SolaRandom.nextFloat() * sideLength));

      // random position to start from on base
      sideLength = points[0].distance(points[1]);
      direction = points[0].subtract(points[1]).normalize();
      var position = points[1].add(direction.scalar(SolaRandom.nextFloat() * sideLength));

      return new EmissionDetails(
        position,
        velocity.normalize()
      );
    }

    return super.nextEmission();
  }

  /**
   * Sets whether particles should be emitted from the first base of the trapezoid.
   *
   * @param emitFromBase whether particles should be emitted from the first base of the trapezoid
   * @return this
   */
  public TrapezoidEmitterShape setEmitFromBase(boolean emitFromBase) {
    this.isEmitFromBase = emitFromBase;

    return this;
  }

  @Override
  protected Vector2D getCenter() {
    return center;
  }

  @Override
  protected Vector2D randomPointInShape() {
    var boundingBox = this.boundingBox;
    var trapezoid = this.trapezoid;

    while (true) {
      float x = SolaRandom.nextFloat(boundingBox.min().x(), boundingBox.max().x());
      float y = SolaRandom.nextFloat(boundingBox.min().y(), boundingBox.max().y());

      if (trapezoid.contains(new Vector2D(x, y))) {
        return new Vector2D(x, y);
      }
    }
  }

  @Override
  protected Vector2D randomPointOnPerimeter() {
    int side = SolaRandom.nextInt(4);
    var points = trapezoid.points();

    var p1 = points[side];
    var p2 = points[(side + 1) % 4];
    var sideLength = p1.distance(p2);
    var direction = p2.subtract(p1).normalize();

    return p1.add(direction.scalar(SolaRandom.nextFloat() * sideLength));
  }
}
