package technology.sola.engine.physics;

import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.event.CollisionManifoldEvent;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.engine.physics.system.GravitySystem;
import technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem;
import technology.sola.engine.physics.system.PhysicsSystem;

public class SolaPhysics {
  private final GravitySystem gravitySystem;
  private final PhysicsSystem physicsSystem;
  private final CollisionDetectionSystem collisionDetectionSystem;
  private final ImpulseCollisionResolutionSystem impulseCollisionResolutionSystem;

  public SolaPhysics(EventHub eventHub) {
    gravitySystem = new GravitySystem();
    physicsSystem = new PhysicsSystem();
    collisionDetectionSystem = new CollisionDetectionSystem();
    impulseCollisionResolutionSystem = new ImpulseCollisionResolutionSystem();

    // TODO possibly clean this up so events aren't registered during constructor
    // TODO maybe some init method that also adds ecs systems???
    eventHub.add(gravitySystem, CollisionManifoldEvent.class);
    eventHub.add(impulseCollisionResolutionSystem, CollisionManifoldEvent.class);

    collisionDetectionSystem.setEmitCollisionEvent(eventHub::emit);
  }

  public AbstractEcsSystem[] getAllPhysicsEcsSystems() {
    return new AbstractEcsSystem[] {
      gravitySystem, physicsSystem, collisionDetectionSystem, impulseCollisionResolutionSystem,
    };
  }

  public GravitySystem getGravitySystem() {
    return gravitySystem;
  }

  public PhysicsSystem getPhysicsSystem() {
    return physicsSystem;
  }

  public CollisionDetectionSystem getCollisionDetectionSystem() {
    return collisionDetectionSystem;
  }

  public ImpulseCollisionResolutionSystem getImpulseCollisionResolutionSystem() {
    return impulseCollisionResolutionSystem;
  }
}
