package technology.sola.engine.ecs.io;

import technology.sola.engine.ecs.World;

public interface WorldSerializer {
  String stringify(World world);

  World parse(String worldString);
}