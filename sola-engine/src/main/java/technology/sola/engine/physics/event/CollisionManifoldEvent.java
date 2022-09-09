package technology.sola.engine.physics.event;

import technology.sola.engine.event.Event;
import technology.sola.engine.physics.CollisionManifold;

public record CollisionManifoldEvent(CollisionManifold collisionManifold) implements Event {
}
