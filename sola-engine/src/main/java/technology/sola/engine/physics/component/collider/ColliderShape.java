package technology.sola.engine.physics.component.collider;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Shape;

/**
 * ColliderShape contains methods and data needed for geometric {@link Shape}s for collision calculations.
 *
 * @param <T> the type of {@link Shape}
 */
@NullMarked
public interface ColliderShape<T extends Shape> {
  /**
   * Gets the {@link ColliderType} of this collider.
   *
   * @return the {@code ColliderType} of this collider
   */
  ColliderType type();

  /**
   * Returns the bounding box for this {@link ColliderShape}.
   *
   * @param transformComponent the {@link TransformComponent} of the entity
   * @param offsetX the x offset of the collider
   * @param offsetY the y offset of the collider
   * @return the bounding box
   */
  Rectangle getBoundingBox(TransformComponent transformComponent, float offsetX, float offsetY);

  /**
   * Gets the geometric {@link Shape} for this collider shape.
   *
   * @param transformComponent the {@link TransformComponent} for the {@link technology.sola.ecs.Entity}
   * @param offsetX            the x offset off the collider
   * @param offsetY            the y offset of the collider
   * @return the geometric {@link Shape}
   */
  T getShape(TransformComponent transformComponent, float offsetX, float offsetY);

  /**
   * Renders debug information for the collider shape.
   *
   * @param renderer           the {@link Renderer}
   * @param transformComponent the {@link technology.sola.ecs.Entity}'s {@link TransformComponent}
   * @param offsetX            the collider shape x-axis offset
   * @param offsetY            the collider shape y-axis offset
   */
  void debugRender(Renderer renderer, TransformComponent transformComponent, float offsetX, float offsetY);
}
