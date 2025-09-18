package technology.sola.engine.physics;

import org.jspecify.annotations.NullMarked;
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

  private SolaPhysics(EventHub eventHub) {
    gravitySystem = new GravitySystem(eventHub);
    physicsSystem = new PhysicsSystem();
    collisionDetectionSystem = new CollisionDetectionSystem(eventHub);
    impulseCollisionResolutionSystem = new ImpulseCollisionResolutionSystem(eventHub);
    particleSystem = new ParticleSystem();
  }

  /**
   * A builder for {@link SolaPhysics}.
   */
  public static class Builder {
    private final SolaEcs solaEcs;
    private boolean withParticleSystem = true;
    private boolean withCollisionDetection = true;

    /**
     * Creates an instance of the builder.
     *
     * @param solaEcs the {@link SolaEcs} instance to attach systems to
     */
    public Builder(SolaEcs solaEcs) {
      this.solaEcs = solaEcs;
    }

    /**
     * Turns off particle systems.
     *
     * @return this
     */
    public Builder withoutParticles() {
      this.withParticleSystem = false;
      return this;
    }

    /**
     * Turns off collision detection systems.
     *
     * @return this
     */
    public Builder withoutCollisionDetection() {
      this.withCollisionDetection = false;
      return this;
    }

    /**
     * Builds and initializes the {@link SolaPhysics} instance.
     *
     * @param eventHub the {@link EventHub} instance to attach systems to
     * @return the {@link SolaPhysics} instance
     */
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
