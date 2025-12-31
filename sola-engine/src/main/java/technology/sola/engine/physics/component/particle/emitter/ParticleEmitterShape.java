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
  public abstract EmissionDetails nextEmission();

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
   * @return a normalized and randomized direction vector
   */
  protected Vector2D randomDirection() {
    var angle = SolaRandom.nextFloat() * 2 * (float) Math.PI;

    return new Vector2D((float) Math.cos(angle), (float) Math.sin(angle));
  }

  /**
   * Holds the position and direction details for a particle to be emitted.
   *
   * @param position  the position of the particle to be emitted
   * @param direction the direction of the particle to be emitted
   */
  public record EmissionDetails(
    Vector2D position,
    Vector2D direction
  ) {
  }
}
