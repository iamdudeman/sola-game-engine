package technology.sola.engine.ecs;

import technology.sola.engine.annotation.NotNull;
import technology.sola.engine.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Entity {
  final int entityIndex;
  private final World world;
  private List<Class<? extends Component>> currentComponents = new ArrayList<>();
  private String name = null;

  /**
   * Gets the integer id of this Entity.
   *
   * @return the id of this Entity
   */
  public int getId() {
    return entityIndex;
  }

  /**
   * Gets the name of this Entity. A name is optional and will be null if not manually set.
   *
   * @return the name of the Entity
   */
  public @Nullable String getName() {
    return name;
  }

  /**
   * Sets the name of this Entity. A name can be used to look up an Entity via {@link World#getEntityByName(String)}.
   *
   * @param name  the name of the Entity
   * @return this Entity
   */
  public Entity setName(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Queues this Entity for destruction. This typically should happen at the end of the current frame.
   */
  public void destroy() {
    world.queueEntityForDestruction(this);
  }

  /**
   * Adds a {@link Component} to this Entity.
   *
   * @param component  the {@code Component} to add
   * @return this Entity
   */
  public Entity addComponent(@NotNull Component component) {
    world.addComponentForEntity(entityIndex, component);
    currentComponents.add(component.getClass());
    return this;
  }

  /**
   * Gets a {@link Component} instance for the class passed in.
   *
   * @param componentClass  the class of the {@code Component} to get
   * @param <T>  the type of the {@code Component} class to get
   * @return the {@code Component} instance or null
   */
  public @Nullable <T extends Component> T getComponent(@NotNull Class<T> componentClass) {
    return world.getComponentForEntity(entityIndex, componentClass);
  }

  /**
   * Gets an {@link Optional} for the class passed in.
   *
   * @param componentClass  the class of the {@code Component} to get
   * @param <T>  the type of the {@code Component} class to get
   * @return the {@code Optional}
   */
  public <T extends Component> Optional<T> getOptionalComponent(@NotNull Class<T> componentClass) {
    return Optional.ofNullable(world.getComponentForEntity(entityIndex, componentClass));
  }

  /**
   * Removes a {@link Component} from this Entity.
   *
   * @param componentClassToRemove  the Class of the {@code Component} to remove
   */
  public void removeComponent(@NotNull Class<? extends Component> componentClassToRemove) {
    world.removeComponent(entityIndex, componentClassToRemove);
    currentComponents = currentComponents.stream().filter(currentComponentClass -> componentClassToRemove != currentComponentClass).collect(Collectors.toList());
  }

  public List<Class<? extends Component>> getCurrentComponents() {
    return currentComponents;
  }

  Entity(World world, int entityIndex) {
    this.world = world;
    this.entityIndex = entityIndex;
  }

}
