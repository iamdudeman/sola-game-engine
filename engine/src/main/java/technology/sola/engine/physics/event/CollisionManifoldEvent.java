package technology.sola.engine.physics.event;

import technology.sola.engine.event.Event;
import technology.sola.engine.physics.CollisionManifold;

public class CollisionManifoldEvent implements Event<CollisionManifold> {
  private CollisionManifold collisionManifold;

  public CollisionManifoldEvent(CollisionManifold collisionManifold) {
    this.collisionManifold = collisionManifold;
  }

  @Override
  public CollisionManifold getMessage() {
    return collisionManifold;
  }
}
