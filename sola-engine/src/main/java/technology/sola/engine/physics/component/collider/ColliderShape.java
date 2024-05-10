package technology.sola.engine.physics.component.collider;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.geometry.Shape;

/**
 * ColliderShape contains methods and data needed for geometric {@link Shape}s for collision calculations.
 *
 * @param <T> the type of {@link Shape}
 */
public interface ColliderShape<T extends Shape> {
  /**
   * Gets the {@link ColliderType} of this collider.
   *
   * @return the {@code ColliderType} of this collider
   */
  ColliderType type();

  /**
   * Gets the width of the bounding box around this collider.
   *
   * @param transformScaleX the x-axis scale of the transform
   * @return the bounding box width of this collider
   */
  float getBoundingWidth(float transformScaleX);

  /**
   * Gets the height of the bounding box around this collider.
   *
   * @param transformScaleY the y-axis scale of the transform
   * @return the bounding box height of this collider
   */
  float getBoundingHeight(float transformScaleY);

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
