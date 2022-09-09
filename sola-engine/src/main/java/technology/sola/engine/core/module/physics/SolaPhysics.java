package technology.sola.engine.core.module.physics;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.core.module.SolaModule;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.engine.physics.system.GravitySystem;
import technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem;
import technology.sola.engine.physics.system.ParticleSystem;
import technology.sola.engine.physics.system.PhysicsSystem;

@SolaModule
public class SolaPhysics {
  private final GravitySystem gravitySystem;
  private final PhysicsSystem physicsSystem;
  private final CollisionDetectionSystem collisionDetectionSystem;
  private final ImpulseCollisionResolutionSystem impulseCollisionResolutionSystem;
  private final ParticleSystem particleSystem;

  public static SolaPhysics createInstance(EventHub eventHub, SolaEcs solaEcs) {
    SolaPhysics solaPhysics = new SolaPhysics(eventHub);

    solaEcs.addSystems(
      solaPhysics.gravitySystem,
      solaPhysics.physicsSystem,
      solaPhysics.collisionDetectionSystem,
      solaPhysics.impulseCollisionResolutionSystem,
      solaPhysics.particleSystem
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

  public ParticleSystem getParticleSystem() {
    return particleSystem;
  }

  private SolaPhysics(EventHub eventHub) {
    gravitySystem = new GravitySystem(eventHub);
    physicsSystem = new PhysicsSystem();
    collisionDetectionSystem = new CollisionDetectionSystem(eventHub);
    impulseCollisionResolutionSystem = new ImpulseCollisionResolutionSystem(eventHub);
    particleSystem = new ParticleSystem();
  }
}
