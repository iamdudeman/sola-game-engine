package technology.sola.engine.core.physics;

import technology.sola.engine.ecs.SolaEcs;
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

  public static SolaPhysics use(EventHub eventHub, SolaEcs solaEcs) {
    SolaPhysics solaPhysics = new SolaPhysics();

    eventHub.add(solaPhysics.gravitySystem, CollisionManifoldEvent.class);
    eventHub.add(solaPhysics.impulseCollisionResolutionSystem, CollisionManifoldEvent.class);

    solaPhysics.collisionDetectionSystem.setEmitCollisionEvent(eventHub::emit);

    solaEcs.addSystems(
      solaPhysics.gravitySystem, solaPhysics.physicsSystem, solaPhysics.collisionDetectionSystem, solaPhysics.impulseCollisionResolutionSystem
    );

    return solaPhysics;
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

  private SolaPhysics() {
    gravitySystem = new GravitySystem();
    physicsSystem = new PhysicsSystem();
    collisionDetectionSystem = new CollisionDetectionSystem();
    impulseCollisionResolutionSystem = new ImpulseCollisionResolutionSystem();
  }
}
