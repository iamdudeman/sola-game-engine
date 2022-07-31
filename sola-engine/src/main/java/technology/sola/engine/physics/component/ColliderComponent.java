package technology.sola.engine.physics.component;

import technology.sola.ecs.Component;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.io.Serial;

public class ColliderComponent implements Component {
  @Serial
  private static final long serialVersionUID = 6393414888672495864L;
  // Properties for all
  private ColliderType colliderType;

  // Properties for circle
  private Float radius = null;

  // Properties for rectangle
  private Float width = null;
  private Float height = null;

  public static ColliderComponent aabb() {
    return aabb(1, 1);
  }

  /**
   * Creates a rectangle {@link ColliderComponent} with defined width and height.
   *
   * @param width  the width of the collision box
   * @param height  the height of the collision box
   * @return a rectangle {@code ColliderComponent}
   */
  public static ColliderComponent aabb(float width, float height) {
    ColliderComponent colliderComponent = new ColliderComponent();

    colliderComponent.colliderType = ColliderType.AABB;
    colliderComponent.width = width;
    colliderComponent.height = height;

    return colliderComponent;
  }

  public static ColliderComponent circle() {
    return circle(0.5f);
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
    return switch (colliderType) {
      case AABB -> width;
      case CIRCLE -> radius * 2;
    };
  }

  /**
   * Gets the height of the bounding box around this collider.
   *
   * @return the bounding box height of this collider
   */
  public float getBoundingHeight() {
    return switch (colliderType) {
      case AABB -> height;
      case CIRCLE -> radius * 2;
    };
  }

  /**
   * Calculates the {@link Circle} representation of this collider based on the position.
   *
   * @param transformComponent  the transform of the {@link technology.sola.ecs.Entity}
   * @return the {@code Circle} representation of this collider
   */
  public Circle asCircle(TransformComponent transformComponent) {
    if (!ColliderType.CIRCLE.equals(colliderType)) {
      throw new ColliderComponentException(colliderType, ColliderType.CIRCLE);
    }

    float transformScale = Math.max(transformComponent.getScaleX(), transformComponent.getScaleY());
    float radiusWithTransform = radius * transformScale;

    return new Circle(radiusWithTransform, transformComponent.getTranslate().add(new Vector2D(radiusWithTransform, radiusWithTransform)));
  }

  /**
   * Calculates the {@link Rectangle} representation of this collider based on the position.
   *
   * @param transformComponent  the transform of the {@link technology.sola.ecs.Entity}
   * @return the {@code Rectangle} representation of  this collider
   */
  public Rectangle asRectangle(TransformComponent transformComponent) {
    if (!ColliderType.AABB.equals(colliderType)) {
      throw new ColliderComponentException(colliderType, ColliderType.AABB);
    }

    return new Rectangle(
      transformComponent.getTranslate(),
      transformComponent.getTranslate().add(new Vector2D(transformComponent.getScaleX() * width, transformComponent.getScaleY() * height))
    );
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
