package technology.sola.engine.physics.system;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.EcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.math.linear.Vector2D;

public class PhysicsSystem extends EcsSystem {
  public static final int ORDER = 10;

  @Override
  public int getOrder() {
    return ORDER;
  }

  @Override
  public void update(World world, float deltaTime) {
    world.getView().of(TransformComponent.class, DynamicBodyComponent.class)
      .forEach(view -> {
        TransformComponent transformComponent = view.getC1();
        DynamicBodyComponent dynamicBodyComponent = view.getC2();

        if (!dynamicBodyComponent.isKinematic()) {
          Vector2D acceleration = new Vector2D(dynamicBodyComponent.getForceX(), dynamicBodyComponent.getForceY())
            .scalar(dynamicBodyComponent.getMaterial().getInverseMass());

          dynamicBodyComponent.setForceX(0);
          dynamicBodyComponent.setForceY(0);
          dynamicBodyComponent.setVelocity(dynamicBodyComponent.getVelocity().add(acceleration.scalar(deltaTime)));
        }

        transformComponent.setTranslate(transformComponent.getTranslate().add(dynamicBodyComponent.getVelocity().scalar(deltaTime)));
      });
  }
}
