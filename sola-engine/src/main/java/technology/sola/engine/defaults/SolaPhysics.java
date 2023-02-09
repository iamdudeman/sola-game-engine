package technology.sola.engine.defaults;

import technology.sola.ecs.EcsSystem;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.engine.physics.system.GravitySystem;
import technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem;
import technology.sola.engine.physics.system.ParticleSystem;
import technology.sola.engine.physics.system.PhysicsSystem;

/**
 * SolaPhysics configures several physics systems in a default configuration.
 */
public class SolaPhysics {
  private final GravitySystem gravitySystem;
  private final PhysicsSystem physicsSystem;
  private final CollisionDetectionSystem collisionDetectionSystem;
  private final ImpulseCollisionResolutionSystem impulseCollisionResolutionSystem;
  private final ParticleSystem particleSystem;

  public SolaPhysics(EventHub eventHub) {
    gravitySystem = new GravitySystem(eventHub);
    physicsSystem = new PhysicsSystem();
    collisionDetectionSystem = new CollisionDetectionSystem(eventHub);
    impulseCollisionResolutionSystem = new ImpulseCollisionResolutionSystem(eventHub);
    particleSystem = new ParticleSystem();
  }

  public EcsSystem[] getSystems() {
    return new EcsSystem[] {
      gravitySystem,
      physicsSystem,
      collisionDetectionSystem,
      impulseCollisionResolutionSystem,
      particleSystem,
    };
  }

  /**
   * @return the {@link GravitySystem}
   */
  public GravitySystem getGravitySystem() {
    return gravitySystem;
  }

  /**
   * @return the {@link PhysicsSystem}
   */
  public PhysicsSystem getPhysicsSystem() {
    return physicsSystem;
  }

  /**
   * @return the {@link CollisionDetectionSystem}
   */
  public CollisionDetectionSystem getCollisionDetectionSystem() {
    return collisionDetectionSystem;
  }

  /**
   * @return the {@link ImpulseCollisionResolutionSystem}
   */
  public ImpulseCollisionResolutionSystem getImpulseCollisionResolutionSystem() {
    return impulseCollisionResolutionSystem;
  }

  /**
   * @return the {@link ParticleSystem}
   */
  public ParticleSystem getParticleSystem() {
    return particleSystem;
  }
}
