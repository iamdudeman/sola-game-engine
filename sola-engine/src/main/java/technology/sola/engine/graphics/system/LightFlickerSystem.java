package technology.sola.engine.graphics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.graphics.components.LightComponent;

public class LightFlickerSystem extends EcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    world.createView().of(LightComponent.class).getEntries().forEach(entry -> {
      entry.c1().tickFlicker(deltaTime);
    });
  }
}
