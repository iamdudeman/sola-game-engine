package technology.sola.engine.physics.component.particle.emitter;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.geometry.ConvexPolygon;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

@NullMarked
public class TrapezoidEmitterShape extends ParticleEmitterShape {
  private boolean isEmitFromBase = true;
  private ConvexPolygon trapezoid;
  private Vector2D center;
  private Rectangle boundingBox;

  public TrapezoidEmitterShape(Vector2D direction, float baseWidth, float height,  float otherBaseWidth) {
    Vector2D p1 = new Vector2D(0, 0);
    Vector2D pL = p1.add(direction.normalize().scalar(height));
    Vector2D perpPL = new Vector2D(pL.y(), -pL.x()).normalize();
    Vector2D p2 = p1.add(perpPL.scalar(baseWidth));

    pL = pL.add(p2.subtract(p1).scalar(0.5f));

    Vector2D p3 = pL.add(perpPL.scalar(otherBaseWidth));
    Vector2D p4 = pL.subtract(perpPL.scalar(otherBaseWidth));

    trapezoid = new ConvexPolygon(new Vector2D[] { p1, p2, p3, p4});
    center = trapezoid.getCentroid();
    boundingBox = trapezoid.boundingBox();
  }

  @Override
  public EmissionDetails nextEmission() {
    if (isEmitFromShell()) {
      return super.nextEmission();
    }

    if (isEmitFromBase) {
      // random location to move towards the other base
      var sideLength = trapezoid.points()[2].distance(trapezoid.points()[3]);
      var direction = trapezoid.points()[3].subtract(trapezoid.points()[2]).normalize();
      var velocity = trapezoid.points()[2].add(direction.scalar(SolaRandom.nextFloat() * sideLength));

      // random position to start from on base
      sideLength = trapezoid.points()[0].distance(trapezoid.points()[1]);
      direction = trapezoid.points()[0].subtract(trapezoid.points()[1]).normalize();
      var position = trapezoid.points()[1].add(direction.scalar(SolaRandom.nextFloat() * sideLength));

      return new EmissionDetails(
        position,
        velocity.normalize()
      );
    }

    return super.nextEmission();
  }

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

    var p1 = trapezoid.points()[side];
    var p2 = trapezoid.points()[(side + 1) % 4];
    var sideLength = p1.distance(p2);
    var direction = p2.subtract(p1).normalize();

    return p1.add(direction.scalar(SolaRandom.nextFloat() * sideLength));
  }
}
