package technology.sola.engine.ecs;

import java.util.ArrayList;
import java.util.List;

public class EcsSystemContainer {
  private final List<EcsSystem> ecsSystems = new ArrayList<>();
  private World world = new World(1);

  public World getWorld() {
    return world;
  }

  public void setWorld(World world) {
    this.world = world;
  }

  public void add(EcsSystem...ecsSystems) {
    for (EcsSystem ecsSystem : ecsSystems) {
      add(ecsSystem);
    }
  }

  public void add(EcsSystem ecsSystem) {
    int insertIndex = 0;

    for (EcsSystem orderedUpdateSystem : ecsSystems) {
      if (ecsSystem.getOrder() <= orderedUpdateSystem.getOrder()) {
        break;
      }
      insertIndex++;
    }

    ecsSystems.add(insertIndex, ecsSystem);
  }

  public <T extends EcsSystem> T get(Class<T> updateSystemClass) {
    return ecsSystems.stream()
      .filter(updateSystemClass::isInstance)
      .map(updateSystemClass::cast)
      .findFirst()
      .orElse(null);
  }

  public void update(float deltaTime) {
    ecsSystems.stream()
      .filter(EcsSystem::isActive)
      .iterator()
      .forEachRemaining(updateSystem -> updateSystem.update(world, deltaTime));
    world.cleanupDestroyedEntities();
  }
}
