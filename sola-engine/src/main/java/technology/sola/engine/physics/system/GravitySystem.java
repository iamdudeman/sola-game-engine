package technology.sola.engine.physics.system;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.event.CollisionEvent;

/**
 * GravitySystem is an {@link EcsSystem} that applies a gravity constant of force to all
 * {@link technology.sola.ecs.Entity} that have a {@link DynamicBodyComponent}, are not kinematic and not grounded.
 */
@NullMarked
public class GravitySystem extends EcsSystem {
  /**
   * The order for this system which is one before the {@link PhysicsSystem#ORDER}.
   */
  public static final int ORDER = PhysicsSystem.ORDER - 1;

  private float gravityConstant;

  /**
   * Creates a GravitySystem instance and registers event listeners to {@link CollisionEvent} to handle updating the
   * grounding of dynamic bodies.
   *
   * @param eventHub the {@link EventHub} instance
   */
  public GravitySystem(EventHub eventHub) {
    this(eventHub, 98f);
  }

  /**
   * Creates a GravitySystem instance with desired gravity constant and registers event listeners to
   * {@link CollisionEvent} to handle updating the grounding of dynamic bodies.
   *
   * @param eventHub the {@link EventHub} instance
   * @param gravityConstant the gravity constant to use
   */
  public GravitySystem(EventHub eventHub, float gravityConstant) {
    setGravityConstant(gravityConstant);

    eventHub.add(CollisionEvent.class, this::handleCollisionManifoldEvent);
  }

  @Override
  public void update(World world, float deltaTime) {
    for (var entry : world.createView().of(DynamicBodyComponent.class).getEntries()) {
      DynamicBodyComponent dynamicBodyComponent = entry.c1();

      if (!dynamicBodyComponent.isGrounded() && !dynamicBodyComponent.isKinematic()) {
        dynamicBodyComponent.applyForce(0, gravityConstant * dynamicBodyComponent.getMaterial().getMass());
      }

      dynamicBodyComponent.setGrounded(false);
    }
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  /**
   * @return the gravity constant
   */
  public float getGravityConstant() {
    return gravityConstant;
  }

  /**
   * Updates the gravity constant
   *
   * @param gravityConstant the new gravity constant
   */
  public void setGravityConstant(float gravityConstant) {
    this.gravityConstant = gravityConstant;
  }

  private void handleCollisionManifoldEvent(CollisionEvent event) {
    if (isActive()) {
      CollisionManifold payload = event.collisionManifold();

      if (payload.normal().y() > 0) {
        DynamicBodyComponent dynamicBodyComponent = payload.entityA().getComponent(DynamicBodyComponent.class);

        if (dynamicBodyComponent != null) {
          dynamicBodyComponent.setGrounded(true);
        }
      } else if (payload.normal().y() < 0) {
        DynamicBodyComponent dynamicBodyComponent = payload.entityB().getComponent(DynamicBodyComponent.class);

        if (dynamicBodyComponent != null) {
          dynamicBodyComponent.setGrounded(true);
        }
      }
    }
  }
}
