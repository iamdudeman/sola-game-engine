package technology.sola.engine.examples.common.systems;

import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.examples.common.components.Position;

public class TestSystem extends AbstractEcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    world.getEntitiesWithComponents(Position.class)
      .forEach(entity -> {
        Position position = entity.getComponent(Position.class);

        position.x++;
        position.y++;
      });
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
