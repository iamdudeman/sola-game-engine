package technology.sola.engine.physics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.event.CollisionManifoldEvent;

public class GravitySystem extends EcsSystem {
  public static final int ORDER = PhysicsSystem.ORDER - 1;

  private float gravityConstant;

  public GravitySystem(EventHub eventHub) {
    this(eventHub, 98f);
  }

  public GravitySystem(EventHub eventHub, float gravityConstant) {
    setGravityConstant(gravityConstant);

    eventHub.add(CollisionManifoldEvent.class, this::handleCollisionManifoldEvent);
  }

  @Override
  public void update(World world, float deltaTime) {
    for (var view : world.createView().of(DynamicBodyComponent.class)) {
      DynamicBodyComponent dynamicBodyComponent = view.c1();

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

  public float getGravityConstant() {
    return gravityConstant;
  }

  public void setGravityConstant(float gravityConstant) {
    this.gravityConstant = gravityConstant;
  }

  private void handleCollisionManifoldEvent(CollisionManifoldEvent event) {
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
