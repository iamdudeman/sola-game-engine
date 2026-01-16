package technology.sola.engine.assets.scene;

import technology.sola.ecs.World;
import technology.sola.engine.assets.Asset;

/**
 * Scene is an {@link Asset} that contains a {@link World}.
 *
 * @param world the {@link World} for the scene
 */
public record Scene(
  World world
) implements Asset {
}
