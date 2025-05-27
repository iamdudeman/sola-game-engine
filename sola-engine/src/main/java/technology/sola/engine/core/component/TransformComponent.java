package technology.sola.engine.core.component;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.math.linear.Vector2D;

/**
 * TransformComponent is a {@link Component} containing translate and scale data for an {@link Entity}.
 */
@NullMarked
public class TransformComponent implements Component {
  private float x;
  private float y;
  private float scaleX;
  private float scaleY;
  @Nullable
  private String parentUniqueId = null;
  @Nullable
  private TransformComponent parentTransformComponent = null;

  /**
   * Creates a transform with position at (0, 0) and scale set to 1.
   */
  public TransformComponent() {
    this(0, 0);
  }

  /**
   * Creates a transform with desired position and scale set to 1.
   *
   * @param x the x position coordinate
   * @param y the y position coordinate
   */
  public TransformComponent(float x, float y) {
    this(x, y, 1);
  }

  /**
   * Creates a transform with desired position and scale set to the desired value.
   *
   * @param x     the x position coordinate
   * @param y     the y position coordinate
   * @param scale the scale of the transform
   */
  public TransformComponent(float x, float y, float scale) {
    this(x, y, scale, scale);
  }

  /**
   * Creates a transform with desired position and scale x and y set to the desired values.
   *
   * @param x      the x position coordinate
   * @param y      the y position coordinate
   * @param scaleX the horizontal scale
   * @param scaleY the vertical scale
   */
  public TransformComponent(float x, float y, float scaleX, float scaleY) {
    this.x = x;
    this.y = y;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
  }

  /**
   * Creates a transform that has position based on a parent. It has an offset from the parent (0, 0) and scale set to 1
   * of its parent's scale.
   *
   * @param parent the parent {@link Entity} (must also have a {@link TransformComponent})
   */
  public TransformComponent(Entity parent) {
    this(0, 0, parent);
  }

  /**
   * Creates a transform that has position based on a parent. It has desired offset from the parent and scale set to 1
   * of its parent's scale.
   *
   * @param x      offset x from parent
   * @param y      offset y from parent
   * @param parent the parent {@link Entity} (must also have a {@link TransformComponent})
   */
  public TransformComponent(float x, float y, Entity parent) {
    this(x, y, 1, parent);
  }

  /**
   * Creates a transform that has position based on a parent. It has desired offset from the parent and desired scale
   * of its parent's scale.
   *
   * @param x      offset x from parent
   * @param y      offset y from parent
   * @param scale  the scale of the parent's scale
   * @param parent the parent {@link Entity} (must also have a {@link TransformComponent})
   */
  public TransformComponent(float x, float y, float scale, Entity parent) {
    this(x, y, scale, scale, parent);
  }

  /**
   * Creates a transform that has position based on a parent. It has desired offset from the parent and desired scale
   * of its parent's scale.
   *
   * @param x      offset x from parent
   * @param y      offset y from parent
   * @param scaleX the horizontal scale of the parent's scale
   * @param scaleY the vertical scale of the parent's scale
   * @param parent the parent {@link Entity} (must also have a {@link TransformComponent})
   */
  public TransformComponent(float x, float y, float scaleX, float scaleY, Entity parent) {
    this(x, y, scaleX, scaleY);

    setParent(parent);
  }

  @Override
  public void afterDeserialize(World world) {
    if (parentUniqueId != null) {
      setParent(world.findEntityByUniqueId(parentUniqueId));
    }
  }

  /**
   * @return the x coordinate or x offset of parent
   */
  public float getX() {
    return parentTransformComponent == null ? x : x + parentTransformComponent.getX();
  }

  /**
   * @return the y coordinate or y offset of parent
   */
  public float getY() {
    return parentTransformComponent == null ? y : y + parentTransformComponent.getY();
  }

  /**
   * @return the translation of the transform as a {@link Vector2D}
   */
  public Vector2D getTranslate() {
    return new Vector2D(getX(), getY());
  }

  /**
   * Sets the new translation x or offset x of parent.
   *
   * @param x the new translation x
   */
  public void setX(float x) {
    this.x = x;
  }

  /**
   * Sets the new translation y or offset y of parent.
   *
   * @param y the new translation y
   */
  public void setY(float y) {
    this.y = y;
  }

  /**
   * Sets the translation of the transform.
   *
   * @param translate the new translation
   */
  public void setTranslate(Vector2D translate) {
    setX(translate.x());
    setY(translate.y());
  }

  /**
   * Sets the translation of the transform.
   *
   * @param x the new x translation
   * @param y the new y translation
   */
  public void setTranslate(float x, float y) {
    setX(x);
    setY(y);
  }

  /**
   * @return the horizontal scale
   */
  public float getScaleX() {
    return parentTransformComponent == null ? scaleX : scaleX * parentTransformComponent.scaleX;
  }

  /**
   * @return the vertical scale
   */
  public float getScaleY() {
    return parentTransformComponent == null ? scaleY : scaleY * parentTransformComponent.scaleY;
  }

  /**
   * @return the parent's unique id or null if no parent
   */
  @Nullable
  public String getParentUniqueId() {
    return parentUniqueId;
  }

  /**
   * Sets the scale of the transform.
   *
   * @param scaleX horizontal scale
   * @param scaleY vertical scale
   */
  public void setScale(float scaleX, float scaleY) {
    setScaleX(scaleX);
    setScaleY(scaleY);
  }

  /**
   * Sets the horizontal and vertical scale of the transform to the same value.
   *
   * @param scale the new scale
   */
  public void setScale(float scale) {
    setScaleX(scale);
    setScaleY(scale);
  }

  /**
   * Sets the horizontal scale.
   *
   * @param scaleX the new horizontal scale
   */
  public void setScaleX(float scaleX) {
    this.scaleX = scaleX;
  }

  /**
   * Sets the vertical scale.
   *
   * @param scaleY hte new vertical scale
   */
  public void setScaleY(float scaleY) {
    this.scaleY = scaleY;
  }

  /**
   * Sets the parent of this TransformComponent.
   *
   * @param entity the parent {@link Entity} or null if no parent
   * @return this
   */
  public TransformComponent setParent(@Nullable Entity entity) {
    if (entity == null) {
      parentUniqueId = null;
      parentTransformComponent = null;
    } else {
      parentUniqueId = entity.getUniqueId();
      parentTransformComponent = entity.getComponent(TransformComponent.class);
    }

    return this;
  }
}
