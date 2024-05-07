package technology.sola.engine.physics.component.collider;

import technology.sola.engine.physics.component.ColliderComponent;

public interface ColliderShape {
  /**
   * Gets the {@link ColliderComponent.ColliderType} of this collider.
   *
   * @return the {@code ColliderType} of this collider
   */
  ColliderComponent.ColliderType type();

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
}
