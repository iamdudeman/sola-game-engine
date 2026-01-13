package technology.sola.engine.assets.scene;

import technology.sola.ecs.World;
import technology.sola.engine.assets.Asset;

public record Scene(
  World world
  // todo active EcsSystems
) implements Asset {
}
