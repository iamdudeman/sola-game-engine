package technology.sola.engine.physics.system;

import org.junit.jupiter.api.Test;
import technology.sola.engine.ecs.World;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.component.DynamicBodyComponent;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GravitySystemTest {
  @Test
  void whenUpdatingEntities_shouldApplyForceByMass() {
    GravitySystem gravitySystem = new GravitySystem(10f);
    World world = new World(1);

    DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent(new Material(2));
    world.createEntity().addComponent(dynamicBodyComponent);

    gravitySystem.update(world, 1f);

    assertEquals(0, dynamicBodyComponent.getForceX());
    assertEquals(20, dynamicBodyComponent.getForceY());
  }
}
