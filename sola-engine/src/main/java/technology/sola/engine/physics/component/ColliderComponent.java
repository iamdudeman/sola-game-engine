package technology.sola.engine.physics.component;

import technology.sola.ecs.Component;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

/**
 * ColliderComponent is a {@link Component} that contains collision data for an {@link technology.sola.ecs.Entity}.
 * The currently supported collider types are:
 * <ul>
 *   <li>{@link ColliderComponent.ColliderType#AABB}</li>
 *   <li>{@link ColliderComponent.ColliderType#CIRCLE}</li>
 * </ul>
 */
public class ColliderComponent implements Component {
  // Properties for all
  private ColliderType colliderType;
  private float offsetX;
  private float offsetY;
  private boolean isSensor = false;
  private ColliderTag[] tags = new ColliderTag[0];
  private ColliderTag[] ignoreTags = new ColliderTag[0];

  // Properties for circle
  private Float radius = null;

  // Properties for rectangle
  private Float width = null;
  private Float height = null;

  /**
   * Creates a rectangle {@link ColliderComponent} with a width and height of 1.
   *
   * @return a rectangle {@code ColliderComponent}
   */
  public static ColliderComponent aabb() {
    return aabb(1, 1);
  }

  /**
   * Creates a rectangle {@link ColliderComponent} with defined width and height.
   *
   * @param width  the width of the collision box
   * @param height the height of the collision box
   * @return a rectangle {@code ColliderComponent}
   */
  public static ColliderComponent aabb(float width, float height) {
    return aabb(0, 0, width, height);
  }

  /**
   * Creates a rectangle {@link ColliderComponent} with defined width and height. Its top, left coordinate is offset
   * from the top, left coordinate of the Transform.
   *
   * @param offsetX the x coordinate offset
   * @param offsetY the y coordinate offset
   * @param width   the width of the collision box
   * @param height  the height of the collision box
   * @return a rectangle {@code ColliderComponent}
   */
  public static ColliderComponent aabb(float offsetX, float offsetY, float width, float height) {
    ColliderComponent colliderComponent = new ColliderComponent();

    colliderComponent.offsetX = offsetX;
    colliderComponent.offsetY = offsetY;
    colliderComponent.colliderType = ColliderType.AABB;
    colliderComponent.width = width;
    colliderComponent.height = height;

    return colliderComponent;
  }

  /**
   * Creates a circle {@link ColliderComponent} with a radius of 0.5.
   *
   * @return a circle {@code ColliderComponent}
   */
  public static ColliderComponent circle() {
    return circle(0.5f);
  }

  /**
   * Creates a circle {@link ColliderComponent} with defined radius.
   *
   * @param radius the radius of the collider
   * @return a circle {@code ColliderComponent}
   */
  public static ColliderComponent circle(float radius) {
    return circle(0, 0, radius);
  }

  /**
   * Creates a circle {@link ColliderComponent} with defined radius. Its top, left coordinate is offset  from the
   * top, left coordinate of the Transform.
   *
   * @param offsetX the x coordinate offset
   * @param offsetY the y coordinate offset
   * @param radius  the radius of the collider
   * @return a circle {@code ColliderComponent}
   */
  public static ColliderComponent circle(float offsetX, float offsetY, float radius) {
    ColliderComponent colliderComponent = new ColliderComponent();

    colliderComponent.offsetX = offsetX;
    colliderComponent.offsetY = offsetY;
    colliderComponent.colliderType = ColliderType.CIRCLE;
    colliderComponent.radius = radius;

    return colliderComponent;
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
    return switch (colliderType) {
      case AABB -> width * transformScaleX;
      case CIRCLE -> radius * 2 * transformScaleX;
    };
  }

  /**
   * Gets the height of the bounding box around this collider.
   *
   * @param transformScaleY the y-axis scale of the transform
   * @return the bounding box height of this collider
   */
  public float getBoundingHeight(float transformScaleY) {
    return switch (colliderType) {
      case AABB -> height * transformScaleY;
      case CIRCLE -> radius * 2 * transformScaleY;
    };
  }

  public Rectangle getBoundingRectangle(TransformComponent transformComponent) {
    var min = transformComponent.getTranslate().add(new Vector2D(offsetX, offsetY));
    var widthHeight = new Vector2D(getBoundingWidth(transformComponent.getScaleX()), getBoundingHeight(transformComponent.getScaleY()));

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
   * Calculates the {@link Circle} representation of this collider based on the position.
   *
   * @param transformComponent the transform of the {@link technology.sola.ecs.Entity}
   * @return the {@code Circle} representation of this collider
   */
  public Circle asCircle(TransformComponent transformComponent) {
    if (!ColliderType.CIRCLE.equals(colliderType)) {
      throw new ColliderComponentException(colliderType, ColliderType.CIRCLE);
    }

    float transformScale = Math.max(transformComponent.getScaleX(), transformComponent.getScaleY());
    float radiusWithTransform = radius * transformScale;

    Vector2D center = new Vector2D(
      transformComponent.getX() + radiusWithTransform + offsetX,
      transformComponent.getY() + radiusWithTransform + offsetY
    );

    return new Circle(radiusWithTransform, center);
  }

  /**
   * Calculates the {@link Rectangle} representation of this collider based on the position.
   *
   * @param transformComponent the transform of the {@link technology.sola.ecs.Entity}
   * @return the {@code Rectangle} representation of  this collider
   */
  public Rectangle asRectangle(TransformComponent transformComponent) {
    if (!ColliderType.AABB.equals(colliderType)) {
      throw new ColliderComponentException(colliderType, ColliderType.AABB);
    }

    Vector2D min = new Vector2D(
      transformComponent.getX() + offsetX,
      transformComponent.getY() + offsetY
    );
    Vector2D max = new Vector2D(
      min.x() + transformComponent.getScaleX() * width,
      min.y() + transformComponent.getScaleY() * height
    );

    return new Rectangle(min, max);
  }

  /**
   * Gets the {@link ColliderType} of this collider.
   *
   * @return the {@code ColliderType} of this collider
   */
  public ColliderType getColliderType() {
    return colliderType;
  }

  private ColliderComponent() {
  }

  /**
   * Available collider types.
   */
  public enum ColliderType {
    /**
     * Axis Aligned Bounding Box
     */
    AABB,
    /**
     * Circle
     */
    CIRCLE
  }

  /**
   * Identifier tag for this collider. Can be used to ignore collisions with other colliders with various tags.
   */
  public interface ColliderTag {
  }
}
