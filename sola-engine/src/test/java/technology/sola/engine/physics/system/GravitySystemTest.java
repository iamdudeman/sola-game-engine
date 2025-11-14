package technology.sola.engine.physics.system;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.ecs.World;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.component.DynamicBodyComponent;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GravitySystemTest {
  @Test
  void whenUpdatingEntities_shouldApplyForceByMass() {
    EventHub mockEventHub = Mockito.mock(EventHub.class);

    GravitySystem gravitySystem = new GravitySystem(mockEventHub, 10f);
    World world = new World(1);

    DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent(new Material(2));
    world.createEntity().addComponent(dynamicBodyComponent);

    world.update();

    gravitySystem.update(world, 1f);

    assertEquals(0, dynamicBodyComponent.getForceX());
    assertEquals(20, dynamicBodyComponent.getForceY());
  }
}
