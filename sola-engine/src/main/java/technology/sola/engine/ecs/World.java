package technology.sola.engine.ecs;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class World implements Serializable {
  @Serial
  private static final long serialVersionUID = -4446723129672527365L;
  private final int maxEntityCount;
  private final Entity[] entities;
  private final Map<Class<? extends Component>, Component<?>[]> components = new HashMap<>();
  private int currentEntityIndex = 0;
  private int totalEntityCount = 0;
  private final List<Entity> entitiesToDestroy = new LinkedList<>();
  private final EcsViewFactory ecsViewFactory;

  /**
   * Creates a new World instance with specified max {@link Entity} count.
   *
   * @param maxEntityCount  the maximum number of {@code Entity} in this World, must be greater than 0
   */
  public World(int maxEntityCount) {
    if (maxEntityCount < 1) {
      throw new IllegalArgumentException("maxEntityCannot must be a positive integer greater than 0");
    }

    this.maxEntityCount = maxEntityCount;
    entities = new Entity[maxEntityCount];
    ecsViewFactory = new EcsViewFactory(entities);
  }

  /**
   * Cleans up entities that were queued for destruction. Should be called at the end of a frame.
   */
  public void cleanupDestroyedEntities() {
    entitiesToDestroy.forEach(this::destroyEntity);
    entitiesToDestroy.clear();
  }

  /**
   * Gets the maximum {@link Entity} count for this world.
   *
   * @return the max {@code Entity} count
   */
  public int getMaxEntityCount() {
    return maxEntityCount;
  }

  /**
   * Gets the current total {@link Entity} count for this world.
   *
   * @return the current total {@code Entity} count
   */
  public int getTotalEntityCount() {
    return totalEntityCount;
  }

  /**
   * Creates a new {@link Entity} inside this World.
   * <p>
   * If the total entity count goes above the max number specified in this world then an exception will be thrown.
   *
   * @return a new {@code Entity}
   */
  public Entity createEntity() {
    totalEntityCount++;
    Entity entity = new Entity(this, nextOpenEntityIndex());
    entities[entity.entityIndex] = entity;
    return entity;
  }

  public Entity createEntity(String uuid) {
    Entity entity = createEntity();

    entity.uniqueId = uuid;

    return entity;
  }

  /**
   * Gets an {@link Entity} by id. Throws an exception if not found.
   *
   * @param id  the id of the {@code Entity} to retrieve
   * @return the {@code Entity}
   */
  public Entity getEntityById(int id) {
    Entity entity = entities[id];

    if (entity == null) throw new EcsException("Entity with id [" + id + "] does not exist");

    return entity;
  }

  /**
   * Gets an {@link Entity} by its name.
   *
   * @param name  the name of the {@code Entity}
   * @return the {@code Entity} with desired name
   */
  public Entity getEntityByName(String name) {
    for (Entity entity : entities) {
      if (entity == null) continue;

      if (name.equals(entity.getName())) {
        return entity;
      }
    }

    return null;
  }

  /**
   * Gets a {@link List} of all {@link Entity} in the world even if they are disabled.
   *
   * @return a {@code List} of all {@code Entity}
   */
  public List<Entity> getEntities() {
    return Arrays.stream(entities)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }

  /**
   * Gets a {@link List} of all {@link Entity} in the world that are not disabled.
   *
   * @return a {@code List} of all enabled {@code Entity}
   */
  public List<Entity> getAllEnabledEntities() {
    return Arrays.stream(entities)
      .filter(entity -> entity != null && !entity.isDisabled())
      .collect(Collectors.toList());
  }

  /**
   * Gets a {@link List} of {@link Entity} where each {@code Entity} has all of the {@link Component} classes searched for
   * and is not disabled.
   *
   * @param componentClasses  array of {@code Component} classes each {@code Entity} will have
   * @return a {@code List} of {@code Entity} each having the desired {@code Component}s
   */
  @SafeVarargs
  public final List<Entity> getEntitiesWithComponents(Class<? extends Component<?>> ...componentClasses) {
    List<Entity> entitiesWithAllComponents = new ArrayList<>();

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) continue;

      boolean hasAllClasses = true;

      for (Class<? extends Component<?>> componentClass : componentClasses) {
        if (getComponentForEntity(entity.entityIndex, componentClass) == null) {
          hasAllClasses = false;
          break;
        }
      }

      if (hasAllClasses) {
        entitiesWithAllComponents.add(entity);
      }
    }

    return entitiesWithAllComponents;
  }

  public EcsViewFactory getView() {
    return ecsViewFactory;
  }

  void addComponentForEntity(int entityIndex, Component<?> component) {
    Component<?>[] componentsOfType = components.computeIfAbsent(component.getClass(), key -> new Component[maxEntityCount]);

    componentsOfType[entityIndex] = component;
  }

  <T extends Component<?>> T getComponentForEntity(int entityIndex, Class<T> componentClass) {
    Component<?>[] componentsOfType = components.computeIfAbsent(componentClass, key -> new Component[maxEntityCount]);

    return componentClass.cast(componentsOfType[entityIndex]);
  }

  void removeComponent(int entityIndex, Class<? extends Component> componentClass) {
    components.computeIfPresent(componentClass, (key, value) -> {
      value[entityIndex] = null;
      return value;
    });
  }

  void queueEntityForDestruction(Entity entity) {
    if (entity == null) throw new IllegalArgumentException("entity to destroy cannot be null");

    if (!entitiesToDestroy.contains(entity)) {
      entitiesToDestroy.add(entity);
    }
  }

  private void destroyEntity(Entity entity) {
    totalEntityCount--;
    entity.getCurrentComponents().forEach(componentClass -> removeComponent(entity.entityIndex, componentClass));
    entities[entity.entityIndex] = null;
  }

  private int nextOpenEntityIndex() {
    int totalEntityCounter = 1; // Starting at 1 for this entity being created
    while (entities[currentEntityIndex] != null) {
      currentEntityIndex = (currentEntityIndex + 1) % maxEntityCount;
      totalEntityCounter++;
      if (totalEntityCounter > maxEntityCount) {
        throw new EcsException("Entity array is filled. No more entities can be created!");
      }
    }

    return currentEntityIndex;
  }
}
