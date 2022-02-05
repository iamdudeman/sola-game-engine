package technology.sola.engine.ecs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SolaEcs {
  private final List<EcsSystem> ecsSystems = new ArrayList<>();
  private World world = new World(1);

  public World getWorld() {
    return world;
  }

  public void setWorld(World world) {
    this.world = world;
  }

  public void addSystems(EcsSystem...ecsSystems) {
    for (EcsSystem ecsSystem : ecsSystems) {
      addSystem(ecsSystem);
    }
  }

  public void addSystem(EcsSystem ecsSystem) {
    int insertIndex = 0;

    for (EcsSystem orderedUpdateSystem : ecsSystems) {
      if (ecsSystem.getOrder() <= orderedUpdateSystem.getOrder()) {
        break;
      }
      insertIndex++;
    }

    ecsSystems.add(insertIndex, ecsSystem);
  }

  public <T extends EcsSystem> T getSystem(Class<T> updateSystemClass) {
    return ecsSystems.stream()
      .filter(updateSystemClass::isInstance)
      .map(updateSystemClass::cast)
      .findFirst()
      .orElse(null);
  }

  public Iterator<EcsSystem> systemIterator() {
    return ecsSystems.iterator();
  }

  public Iterator<EcsSystem> activeSystemIterator() {
    return ecsSystems.stream()
      .filter(EcsSystem::isActive)
      .iterator();
  }

  public void updateWorld(float deltaTime) {
    ecsSystems.stream()
      .filter(EcsSystem::isActive)
      .iterator()
      .forEachRemaining(updateSystem -> updateSystem.update(world, deltaTime));
    world.cleanupDestroyedEntities();
  }
}
