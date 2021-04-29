package technology.sola.engine.ecs;

import technology.sola.engine.annotation.NotNull;

public abstract class AbstractSystem {
  private boolean isActive = true;

  /**
   * Called to update the state of {@link Component}s attached to an {@link Entity}.
   * <p>
   * Typically they will call {@link World#getEntitiesWithComponents(Class[])} to get {@code Entity} instances that have
   * a particular {@code Component} set.
   *
   * @param world  the {@code World}
   * @param deltaTime  the delta time between the last frame and the current frame
   */
  public abstract void update(@NotNull World world, float deltaTime);

  public final boolean isActive() {
    return isActive;
  }

  public final void setActive(boolean isActive) {
    this.isActive = isActive;
  }
}
