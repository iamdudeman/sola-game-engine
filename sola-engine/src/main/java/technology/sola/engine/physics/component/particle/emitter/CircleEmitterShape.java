package technology.sola.engine.physics.component.particle.emitter;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.linear.Vector2D;

@NullMarked
public class CircleEmitterShape extends ParticleEmitterShape {
  private float radius;
  private Vector2D center;

  public CircleEmitterShape(float radius) {
    setRadius(radius);
  }

  @Override
  public EmissionDetails nextEmission() {
    var position = isEmitFromShell()
      ? getRandomPointOnCircle()
      : getRandomPointInCircle();
    var direction = isRandomDirection()
      ? randomDirection()
      : position.subtract(center).normalize();

    return new EmissionDetails(position, direction);
  }

  public float getRadius() {
    return radius;
  }

  public void setRadius(float radius) {
    this.radius = radius;

    center = new Vector2D(radius, radius);
  }

  private Vector2D getRandomPointInCircle() {
    var r = radius * (float) Math.sqrt(SolaRandom.nextFloat());

    return getRandomPoint(r);
  }

  private Vector2D getRandomPointOnCircle() {
    return getRandomPoint(radius);
  }

  private Vector2D getRandomPoint(float radius) {
    var angle = SolaRandom.nextFloat() * 2 * (float) Math.PI;

    return new Vector2D(
      center.x() + radius * (float) Math.cos(angle),
      center.y() + radius * (float) Math.sin(angle)
    );
  }
}
