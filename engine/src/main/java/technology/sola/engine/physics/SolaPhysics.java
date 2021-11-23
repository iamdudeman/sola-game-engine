package technology.sola.engine.physics;

import technology.sola.engine.ecs.EcsSystemContainer;
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

    eventHub.add(gravitySystem, CollisionManifoldEvent.class);
    eventHub.add(impulseCollisionResolutionSystem, CollisionManifoldEvent.class);

    collisionDetectionSystem.setEmitCollisionEvent(eventHub::emit);
  }

  // TODO consider getEcsSystems method instead so this doesn't depend on EcsSystemContainer
  public void addEcsSystems(EcsSystemContainer ecsSystemContainer) {
    ecsSystemContainer.add(gravitySystem);
    ecsSystemContainer.add(physicsSystem);
    ecsSystemContainer.add(collisionDetectionSystem);
    ecsSystemContainer.add(impulseCollisionResolutionSystem);
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
