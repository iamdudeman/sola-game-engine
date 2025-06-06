package technology.sola.engine.physics.system;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.math.linear.Vector2D;

/**
 * PhysicsSystem is an {@link EcsSystem} that handles updating {@link technology.sola.ecs.Entity} with a
 * {@link TransformComponent} and {@link DynamicBodyComponent}. It will update the velocity based on its applied force
 * unless it is kinematic. Then it will update the translation based on the velocity scaled to deltaTime.
 */
@NullMarked
public class PhysicsSystem extends EcsSystem {
  /**
   * The order of this system.
   */
  public static final int ORDER = 10;

  @Override
  public int getOrder() {
    return ORDER;
  }

  @Override
  public void update(World world, float deltaTime) {
    for (var view : world.createView().of(TransformComponent.class, DynamicBodyComponent.class).getEntries()) {
      TransformComponent transformComponent = view.c1();
      DynamicBodyComponent dynamicBodyComponent = view.c2();

      if (!dynamicBodyComponent.isKinematic()) {
        Vector2D acceleration = new Vector2D(dynamicBodyComponent.getForceX(), dynamicBodyComponent.getForceY())
          .scalar(dynamicBodyComponent.getMaterial().getInverseMass());

        dynamicBodyComponent.setForceX(0);
        dynamicBodyComponent.setForceY(0);
        dynamicBodyComponent.setVelocity(dynamicBodyComponent.getVelocity().add(acceleration.scalar(deltaTime)));
      }

      transformComponent.setTranslate(transformComponent.getTranslate().add(dynamicBodyComponent.getVelocity().scalar(deltaTime)));
    }
  }
}
