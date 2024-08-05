package technology.sola.engine.physics;

import technology.sola.math.linear.Vector2D;

/**
 * The minimum translation vector contains the information needed to resolve a collision.
 *
 * @param normal      the collision normal
 * @param penetration the penetration of the collision
 */
public record MinimumTranslationVector(Vector2D normal, float penetration) {
}
