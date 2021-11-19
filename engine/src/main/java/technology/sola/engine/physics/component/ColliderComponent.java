package technology.sola.engine.physics.component;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.Component;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

public class ColliderComponent implements Component {
  // Properties for all
  private ColliderType colliderType;

  // Properties for circle
  private Float radius = null;

  // Properties for rectangle
  private Float width = null;
  private Float height = null;

  /**
   * Creates a rectangle {@link ColliderComponent} with defined width and height.
   *
   * @param width  the width of the collision box
   * @param height  the height of the collision box
   * @return a rectangle {@code ColliderComponent}
   */
  public static ColliderComponent rectangle(float width, float height) {
    ColliderComponent colliderComponent = new ColliderComponent();

    colliderComponent.colliderType = ColliderType.AABB;
    colliderComponent.width = width;
    colliderComponent.height = height;

    return colliderComponent;
  }

  /**
   * Creates a circle {@link ColliderComponent} with defined radius.
   *
   * @param radius  the radius of the collider
   * @return a circle {@code ColliderComponent}
   */
  public static ColliderComponent circle(float radius) {
    ColliderComponent colliderComponent = new ColliderComponent();

    colliderComponent.colliderType = ColliderType.CIRCLE;
    colliderComponent.radius = radius;

    return colliderComponent;
  }

  /**
   * Gets the width of the bounding box around this collider.
   *
   * @return the bounding box width of this collider
   */
  public float getBoundingWidth() {
    switch (colliderType) {
      case AABB: return width;
      case CIRCLE: return radius * 2;
      default: return 0;
    }
  }

  /**
   * Gets the height of the bounding box around this collider.
   *
   * @return the bounding box height of this collider
   */
  public float getBoundingHeight() {
    switch (colliderType) {
      case AABB: return height;
      case CIRCLE: return radius * 2;
      default: return 0;
    }
  }

  /**
   * Calculates the {@link Circle} representation of this collider based on the position.
   *
   * @param transformComponent  the transform of the {@link technology.sola.engine.ecs.Entity}
   * @return the {@code Circle} representation of this collider
   */
  public Circle asCircle(TransformComponent transformComponent) {
    if (!ColliderType.CIRCLE.equals(colliderType)) {
      throw new ColliderComponentException(colliderType, ColliderType.CIRCLE);
    }

    return new Circle(radius, transformComponent.getTranslate().add(new Vector2D(radius, radius)));
  }

  /**
   * Calculates the {@link Rectangle} representation of this collider based on the position.
   *
   * @param transformComponent  the transform of the {@link technology.sola.engine.ecs.Entity}
   * @return the {@code Rectangle} representation of  this collider
   */
  public Rectangle asRectangle(TransformComponent transformComponent) {
    return new Rectangle(transformComponent.getTranslate(), transformComponent.getTranslate().add(new Vector2D(width, height)));
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
    /** Axis Aligned Bounding Box */
    AABB,
    /** Circle */
    CIRCLE
  }
}
