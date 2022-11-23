package technology.sola.engine.physics.event;

import technology.sola.engine.event.Event;
import technology.sola.engine.physics.CollisionManifold;

/**
 * {@link Event} that a collision happened but will not be resolved by applying any forces.
 *
 * @param collisionManifold the {@link CollisionManifold} for the collision that was detected
 */
public record SensorEvent(CollisionManifold collisionManifold) implements Event {
}
