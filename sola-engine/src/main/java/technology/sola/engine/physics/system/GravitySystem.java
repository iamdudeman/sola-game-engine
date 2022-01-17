package technology.sola.engine.physics.system;

import technology.sola.engine.ecs.EcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.event.EventListener;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.event.CollisionManifoldEvent;

public class GravitySystem extends EcsSystem implements EventListener<CollisionManifoldEvent> {
  public static final int ORDER = PhysicsSystem.ORDER - 1;

  private float gravityConstant;

  public GravitySystem() {
    this(98f);
  }

  public GravitySystem(float gravityConstant) {
    setGravityConstant(gravityConstant);
  }

  @Override
  public void update(World world, float deltaTime) {
    world.getEntitiesWithComponents(DynamicBodyComponent.class)
      .forEach(entity -> {
        DynamicBodyComponent dynamicBodyComponent = entity.getComponent(DynamicBodyComponent.class);

        if (!dynamicBodyComponent.isGrounded() && !dynamicBodyComponent.isKinematic()) {
          dynamicBodyComponent.applyForce(0, gravityConstant * dynamicBodyComponent.getMaterial().getMass());
        }

        dynamicBodyComponent.setGrounded(false);
      });
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  @Override
  public void onEvent(CollisionManifoldEvent event) {
    CollisionManifold payload = event.getMessage();

    if (payload.normal().y > 0) {
      DynamicBodyComponent dynamicBodyComponent = payload.entityA().getComponent(DynamicBodyComponent.class);

      if (dynamicBodyComponent != null) {
        dynamicBodyComponent.setGrounded(true);
      }
    } else if (payload.normal().y < 0) {
      DynamicBodyComponent dynamicBodyComponent = payload.entityB().getComponent(DynamicBodyComponent.class);

      if (dynamicBodyComponent != null) {
        dynamicBodyComponent.setGrounded(true);
      }
    }
  }

  public float getGravityConstant() {
    return gravityConstant;
  }

  public void setGravityConstant(float gravityConstant) {
    this.gravityConstant = gravityConstant;
  }
}
