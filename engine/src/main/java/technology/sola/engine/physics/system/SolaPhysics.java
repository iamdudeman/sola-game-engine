package technology.sola.engine.physics.system;

import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.ecs.World;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.physics.event.CollisionManifoldEvent;

public class SolaPhysics {
  private final GravitySystem gravitySystem;
  private final PhysicsSystem physicsSystem;
  private final CollisionDetectionSystem collisionDetectionSystem;
  private final ImpulseCollisionResolutionSystem impulseCollisionResolutionSystem;

  public SolaPhysics(EventHub eventHub) {
    gravitySystem = new GravitySystem(98f);
    physicsSystem = new PhysicsSystem();
    collisionDetectionSystem = new CollisionDetectionSystem();
    impulseCollisionResolutionSystem = new ImpulseCollisionResolutionSystem();

    eventHub.add(gravitySystem, CollisionManifoldEvent.class);
    eventHub.add(impulseCollisionResolutionSystem, CollisionManifoldEvent.class);

    collisionDetectionSystem.setEmitCollisionEvent(eventHub::emit);
  }

  public void applyTo(EcsSystemContainer ecsSystemContainer) {
    ecsSystemContainer.add(gravitySystem);
    ecsSystemContainer.add(physicsSystem);
    ecsSystemContainer.add(collisionDetectionSystem);
    ecsSystemContainer.add(impulseCollisionResolutionSystem);
  }

  public void debugRender(Renderer renderer, World world, Color colliderOutlineColor, Color spacialHashMapCellColor) {
    collisionDetectionSystem.debugRender(renderer, world, colliderOutlineColor, spacialHashMapCellColor);
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
