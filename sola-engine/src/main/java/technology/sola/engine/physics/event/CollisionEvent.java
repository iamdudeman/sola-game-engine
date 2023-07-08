package technology.sola.engine.physics.event;

import technology.sola.engine.event.Event;
import technology.sola.engine.physics.CollisionManifold;

/**
 * {@link Event} that a collision happened that should be resolved by applying forces.
 *
 * @param collisionManifold the {@link CollisionManifold} for the collision that was detected
 */
public record CollisionEvent(CollisionManifold collisionManifold) implements Event {
}
