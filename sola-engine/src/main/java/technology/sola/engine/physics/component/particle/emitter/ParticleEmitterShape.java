package technology.sola.engine.physics.component.particle.emitter;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.linear.Vector2D;

/**
 * ParticleEmitterShape defines the API for a particle emission shape to be used
 * in {@link technology.sola.engine.physics.component.particle.ParticleEmissionConfiguration}.
 */
@NullMarked
public abstract class ParticleEmitterShape {
  private boolean emitFromShell;
  private boolean randomDirection;

  /**
   * Calculates the position and direction details for the next particle emitted.
   *
   * @return the {@link EmissionDetails} for the next particle emitted.
   */
  public EmissionDetails nextEmission() {
    var position = isEmitFromShell()
      ? randomPointOnPerimeter()
      : randomPointInShape();
    var direction = isRandomDirection()
      ? randomDirection()
      : position.subtract(getCenter()).normalize();

    return new EmissionDetails(position, direction);
  }

  /**
   * @return true if particles should only be emitted from the shell of the shape
   */
  public boolean isEmitFromShell() {
    return emitFromShell;
  }

  /**
   * Sets whether particles should only be emitted from the shell of the shape.
   *
   * @param emitFromShell true if particles should only be emitted from the shell of the shape
   * @return this
   */
  public ParticleEmitterShape setEmitFromShell(boolean emitFromShell) {
    this.emitFromShell = emitFromShell;

    return this;
  }

  /**
   * @return true if particles should be emitted in a random direction
   */
  public boolean isRandomDirection() {
    return randomDirection;
  }

  /**
   * Sets whether particles should be emitted in a random direction. Otherwise, particles will be emitted away from the
   * shape's center.
   *
   * @param randomDirection true if particles should be emitted in a random direction
   * @return this
   */
  public ParticleEmitterShape setRandomDirection(boolean randomDirection) {
    this.randomDirection = randomDirection;

    return this;
  }

  /**
   * @return the center point of the shape
   */
  protected abstract Vector2D getCenter();

  /**
   * @return a random point inside the shape
   */
  protected abstract Vector2D randomPointInShape();

  /**
   * @return a random point on the perimeter of the shape
   */
  protected abstract Vector2D randomPointOnPerimeter();

  private Vector2D randomDirection() {
    var angle = SolaRandom.nextFloat() * 2 * (float) Math.PI;

    return new Vector2D((float) Math.cos(angle), (float) Math.sin(angle));
  }
}
