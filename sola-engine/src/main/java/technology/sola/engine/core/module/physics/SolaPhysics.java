package technology.sola.engine.core.module.physics;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.core.module.SolaModule;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.engine.physics.system.GravitySystem;
import technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem;
import technology.sola.engine.physics.system.ParticleSystem;
import technology.sola.engine.physics.system.PhysicsSystem;

/**
 * SolaPhysics is a {@link SolaModule} that adds several {@link technology.sola.ecs.EcsSystem}s with default configurations
 * for handling physics calculations like gravity, collisions and collision resolution.
 */
@SolaModule
public class SolaPhysics {
  private final GravitySystem gravitySystem;
  private final PhysicsSystem physicsSystem;
  private final CollisionDetectionSystem collisionDetectionSystem;
  private final ImpulseCollisionResolutionSystem impulseCollisionResolutionSystem;
  private final ParticleSystem particleSystem;

  /**
   * Creates an instance of {@link SolaPhysics} and adds {@link technology.sola.ecs.EcsSystem}s for physics calculations.
   *
   * @param eventHub {@link EventHub} instance
   * @param solaEcs  {@link SolaEcs} instance
   * @return a new {@code SolaPhysics} instance
   */
  public static SolaPhysics useModule(EventHub eventHub, SolaEcs solaEcs) {
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
