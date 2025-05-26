package technology.sola.engine.graphics.system;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.graphics.components.LightComponent;

/**
 * LightFlickerSystem is an {@link EcsSystem} that handles updating the flicker data of a {@link LightComponent}.
 */
@NullMarked
public class LightFlickerSystem extends EcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    world.createView().of(LightComponent.class)
      .getEntries()
      .forEach(entry -> entry.c1().tickFlicker(deltaTime));
  }
}
