package technology.sola.engine.examples.common.networking;

import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.physics.component.ColliderComponent;

public class LevelBuilder {
  public static World createWorld(int maxPlayers) {
    World world = new World(maxPlayers + 1);

    world.createEntity(
      new TransformComponent(0, 500, 800, 80),
      new RectangleRendererComponent(Color.WHITE),
      ColliderComponent.aabb()
    );

    return world;
  }
}
