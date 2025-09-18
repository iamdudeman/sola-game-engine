package technology.sola.engine.defaults;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.SolaEcs;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.engine.physics.system.GravitySystem;
import technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem;
import technology.sola.engine.physics.system.ParticleSystem;
import technology.sola.engine.physics.system.PhysicsSystem;

/**
 * SolaPhysics configures several physics systems in a default configuration.
 */
@NullMarked
public class SolaPhysics {
  private final GravitySystem gravitySystem;
  private final PhysicsSystem physicsSystem;
  private final CollisionDetectionSystem collisionDetectionSystem;
  private final ImpulseCollisionResolutionSystem impulseCollisionResolutionSystem;
  private final ParticleSystem particleSystem;

  /**
   * Creates a SolaPhysics instance using an {@link EventHub}.
   *
   * @param eventHub the {@code EventHub} to use
   */
  public SolaPhysics(EventHub eventHub) {
    gravitySystem = new GravitySystem(eventHub);
    physicsSystem = new PhysicsSystem();
    collisionDetectionSystem = new CollisionDetectionSystem(eventHub);
    impulseCollisionResolutionSystem = new ImpulseCollisionResolutionSystem(eventHub);
    particleSystem = new ParticleSystem();
  }

  /**
   * @return an array of all physics related {@link EcsSystem}s
   */
  public EcsSystem[] getSystems() {
    return new EcsSystem[]{
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

  public static class Builder {
    private final SolaEcs solaEcs;
    private boolean withParticleSystem = true;
    private boolean withCollisionDetection = true;

    public Builder(SolaEcs solaEcs) {
      this.solaEcs = solaEcs;
    }

    public Builder withParticleSystem(boolean withParticleSystem) {
      this.withParticleSystem = withParticleSystem;
      return this;
    }

    public Builder withCollisionDetection(boolean withCollisionDetection) {
      this.withCollisionDetection = withCollisionDetection;
      return this;
    }

    public SolaPhysics buildAndInitialize(EventHub eventHub) {
      SolaPhysics solaPhysics = new SolaPhysics(eventHub);

      solaEcs.addSystems(
        solaPhysics.gravitySystem,
        solaPhysics.physicsSystem
      );

      if (withCollisionDetection) {
        solaEcs.addSystems(
          solaPhysics.collisionDetectionSystem,
          solaPhysics.impulseCollisionResolutionSystem
        );
      }

      if (withParticleSystem) {
        solaEcs.addSystem(solaPhysics.particleSystem);
      }

      return solaPhysics;
    }
  }
}
