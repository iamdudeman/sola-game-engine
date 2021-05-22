package technology.sola.engine.physics.system;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.engine.ecs.World;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.PositionComponent;
import technology.sola.engine.physics.component.VelocityComponent;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhysicsSystemTest {
  @Nested
  class dynamic {
    @Test
    void whenEntityDynamic_shouldApplyForceAsAcceleration() {
      World world = new World(1);

      PositionComponent positionComponent = new PositionComponent();
      DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent(new Material(2));
      dynamicBodyComponent.applyForce(100, 200);
      world.createEntity()
        .addComponent(positionComponent)
        .addComponent(dynamicBodyComponent)
        .addComponent(new VelocityComponent());

      PhysicsSystem physicsSystem = new PhysicsSystem();

      physicsSystem.update(world, 0.1f);

      assertEquals(0.5f, positionComponent.getX());
      assertEquals(1, positionComponent.getY());
      assertEquals(0, dynamicBodyComponent.getForceX());
      assertEquals(0, dynamicBodyComponent.getForceY());
    }
  }

  @Nested
  class notDynamic {
    @Test
    void whenUpdatingEntities_shouldOnlyApplyVelocityToPosition() {
      World world = new World(1);

      PositionComponent positionComponent = new PositionComponent();
      world.createEntity()
        .addComponent(positionComponent)
        .addComponent(new VelocityComponent(100, 200));

      PhysicsSystem physicsSystem = new PhysicsSystem();

      physicsSystem.update(world, 0.1f);

      assertEquals(10, positionComponent.getX());
      assertEquals(20, positionComponent.getY());
    }
  }
}
