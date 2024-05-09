package technology.sola.engine.examples.common.features.networking;

import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;

/**
 * LevelBuilder contains utilities for building a {@link World} that both server and client can use.
 */
public class LevelBuilder {
  /**
   * Creates the {@link World} for client and server to use.
   *
   * @param maxPlayers the maximum number of players that can be in the world
   * @return the world
   */
  public static World createWorld(int maxPlayers) {
    World world = new World(maxPlayers + 1);

    world.createEntity(
      new TransformComponent(0, 500, 800, 80),
      new RectangleRendererComponent(Color.WHITE),
      new ColliderComponent(new ColliderShapeAABB())
    );

    return world;
  }
}
