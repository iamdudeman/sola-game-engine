package technology.sola.engine.physics.system;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.ecs.World;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.math.linear.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhysicsSystemTest {
  @Nested
  class dynamic {
    @Test
    void whenEntityDynamic_shouldApplyForceAsAcceleration() {
      World world = new World(1);

      TransformComponent transformComponent = new TransformComponent();
      DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent(new Material(2));
      dynamicBodyComponent.applyForce(100, 200);
      world.createEntity()
        .addComponent(transformComponent)
        .addComponent(dynamicBodyComponent);

      PhysicsSystem physicsSystem = new PhysicsSystem();

      physicsSystem.update(world, 0.1f);

      assertEquals(0.5f, transformComponent.getX());
      assertEquals(1, transformComponent.getY());
      assertEquals(0, dynamicBodyComponent.getForceX());
      assertEquals(0, dynamicBodyComponent.getForceY());
    }
  }

  @Nested
  class notDynamic {
    @Test
    void whenUpdatingEntities_shouldOnlyApplyVelocityToPosition() {
      World world = new World(1);

      TransformComponent transformComponent = new TransformComponent();
      DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent();
      dynamicBodyComponent.setVelocity(new Vector2D(100, 200));
      dynamicBodyComponent.applyForce(100, 200);
      dynamicBodyComponent.setKinematic(true);

      world.createEntity()
        .addComponent(transformComponent)
        .addComponent(dynamicBodyComponent);

      PhysicsSystem physicsSystem = new PhysicsSystem();

      physicsSystem.update(world, 0.1f);

      assertEquals(10, transformComponent.getX());
      assertEquals(20, transformComponent.getY());
    }
  }
}
