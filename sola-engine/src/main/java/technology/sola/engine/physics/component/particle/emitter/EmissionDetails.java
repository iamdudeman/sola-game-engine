package technology.sola.engine.physics.component.particle.emitter;

import technology.sola.math.linear.Vector2D;

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
