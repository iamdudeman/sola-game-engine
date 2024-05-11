package technology.sola.engine.physics.component;

import technology.sola.ecs.Component;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.collider.*;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Shape;
import technology.sola.math.linear.Vector2D;

/**
 * ColliderComponent is a {@link Component} that contains collision data for an {@link technology.sola.ecs.Entity}.
 * The currently supported {@link ColliderShape}s are:
 * <ul>
 *   <li>{@link ColliderShapeAABB}</li>
 *   <li>{@link ColliderShapeCircle}</li>
 * </ul>
 */
public class ColliderComponent implements Component {
  private final ColliderShape<?> colliderShape;
  private final float offsetX;
  private final float offsetY;
  private boolean isSensor = false;
  private ColliderTag[] tags = new ColliderTag[0];
  private ColliderTag[] ignoreTags = new ColliderTag[0];

  /**
   * Creates a ColliderComponent instance with specified {@link ColliderShape}.
   *
   * @param colliderShape the {@link ColliderShape}
   */
  public ColliderComponent(ColliderShape<?> colliderShape) {
    this(colliderShape, 0f, 0f);
  }

  /**
   * Creates a ColliderComponent instance with specified {@link ColliderShape} and collider offset.
   *
   * @param colliderShape the {@link ColliderShape}
   * @param offsetX       the collider x-axis offset
   * @param offsetY       the collider y-axis offset
   */
  public ColliderComponent(ColliderShape<?> colliderShape, float offsetX, float offsetY) {
    this.colliderShape = colliderShape;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }

  /**
   * @return the x offset from the transform
   */
  public float getOffsetX() {
    return offsetX;
  }

  /**
   * @return the y offset from the transform
   */
  public float getOffsetY() {
    return offsetY;
  }

  /**
   * Gets the width of the bounding box around this collider.
   *
   * @param transformScaleX the x-axis scale of the transform
   * @return the bounding box width of this collider
   */
  public float getBoundingWidth(float transformScaleX) {
    return colliderShape.getBoundingWidth(transformScaleX);
  }

  /**
   * Gets the height of the bounding box around this collider.
   *
   * @param transformScaleY the y-axis scale of the transform
   * @return the bounding box height of this collider
   */
  public float getBoundingHeight(float transformScaleY) {
    return colliderShape.getBoundingHeight(transformScaleY);
  }

  /**
   * Returns the bounding rectangle for this ColliderComponent.
   *
   * @param transformComponent the {@link TransformComponent} of the {@link technology.sola.ecs.Entity}
   * @return the bounding rectangle
   */
  public Rectangle getBoundingRectangle(TransformComponent transformComponent) {
    var min = transformComponent.getTranslate().add(new Vector2D(offsetX, offsetY));
    var widthHeight = new Vector2D(
      getBoundingWidth(transformComponent.getScaleX()),
      getBoundingHeight(transformComponent.getScaleY())
    );

    return new Rectangle(min, min.add(widthHeight));
  }

  /**
   * A collider that is a sensor will not respond to collision resolution but will emit collision events if an
   * {@link technology.sola.ecs.Entity} with a {@link DynamicBodyComponent} collides.
   *
   * @return true if this collider is a sensor
   */
  public boolean isSensor() {
    return isSensor;
  }

  /**
   * Sets whether this collider is a sensor or not.
   *
   * @param isSensor whether this collider is a sensor or not
   * @return this
   */
  public ColliderComponent setSensor(boolean isSensor) {
    this.isSensor = isSensor;
    return this;
  }

  /**
   * @return the {@link ColliderTag}s for this Collider
   */
  public ColliderTag[] getTags() {
    return tags;
  }

  /**
   * Sets the {@link ColliderTag}s for this collider.
   *
   * @param tags the new tags
   * @return this
   */
  public ColliderComponent setTags(ColliderTag... tags) {
    this.tags = tags;

    return this;
  }

  /**
   * Checks to see if this collider has a {@link ColliderTag}.
   *
   * @param colliderTag the tag to check
   * @return true if collider has tag
   */
  public boolean hasTag(ColliderTag colliderTag) {
    for (ColliderTag tag : tags) {
      if (colliderTag == tag) {
        return true;
      }
    }

    return false;
  }

  /**
   * @return the {@link ColliderTag}s to ignore for this Collider
   */
  public ColliderTag[] getIgnoreTags() {
    return ignoreTags;
  }

  /**
   * Sets the {@link ColliderTag}s to ignore for this collider.
   *
   * @param ignoreTags the new tags to ignore
   * @return this
   */
  public ColliderComponent setIgnoreTags(ColliderTag... ignoreTags) {
    this.ignoreTags = ignoreTags;

    return this;
  }

  /**
   * Checks to see if this collider has a {@link ColliderTag} that it is ignoring.
   *
   * @param colliderTag the tag to check
   * @return true if collider is ignoring tag
   */
  public boolean hasIgnoreColliderTag(ColliderTag colliderTag) {
    for (ColliderTag tag : ignoreTags) {
      if (colliderTag == tag) {
        return true;
      }
    }

    return false;
  }

  /**
   * Gets the geometric {@link Shape} representation of the collider for collision calculations.
   *
   * @param transformComponent the {@link technology.sola.ecs.Entity}'s current {@link TransformComponent}
   * @param <T>                the type of the {@link Shape}
   * @return the {@link Shape} of the collider
   */
  @SuppressWarnings("unchecked")
  public <T extends Shape> T getShape(TransformComponent transformComponent) {
    return (T) colliderShape.getShape(transformComponent, offsetX, offsetY);
  }

  /**
   * Renders a debug overlay over the collider.
   *
   * @param renderer           the {@link Renderer}
   * @param transformComponent the {@link technology.sola.ecs.Entity}'s {@link TransformComponent}
   */
  public void debugRender(Renderer renderer, TransformComponent transformComponent) {
    colliderShape.debugRender(renderer, transformComponent, offsetX, offsetY);
  }

  /**
   * Gets the {@link ColliderType} of this collider.
   *
   * @return the {@code ColliderType} of this collider
   */
  public ColliderType getType() {
    return colliderShape.type();
  }
}
