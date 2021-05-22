package technology.sola.engine.physics.system;

import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.PositionComponent;
import technology.sola.engine.physics.component.VelocityComponent;
import technology.sola.math.linear.Vector2D;

public class PhysicsSystem extends AbstractEcsSystem {
  public static final int ORDER = 10;

  @Override
  public int getOrder() {
    return ORDER;
  }

  @Override
  public void update(World world, float deltaTime) {
    world.getEntitiesWithComponents(PositionComponent.class, VelocityComponent.class)
      .forEach(entity -> {
        PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        VelocityComponent velocityComponent = entity.getComponent(VelocityComponent.class);
        DynamicBodyComponent dynamicBodyComponent = entity.getComponent(DynamicBodyComponent.class);

        if (dynamicBodyComponent != null) {
          Vector2D acceleration = new Vector2D(dynamicBodyComponent.getForceX(), dynamicBodyComponent.getForceY())
            .scalar(dynamicBodyComponent.getMaterial().getInverseMass());

          dynamicBodyComponent.setForceX(0);
          dynamicBodyComponent.setForceY(0);
          velocityComponent.set(velocityComponent.get().add(acceleration.scalar(deltaTime)));
        }

        positionComponent.set(positionComponent.get().add(velocityComponent.get().scalar(deltaTime)));
      });
  }
}
