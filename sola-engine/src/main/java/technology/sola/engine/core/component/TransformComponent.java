package technology.sola.engine.core.component;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.math.linear.Vector2D;

import java.io.Serial;

public class TransformComponent implements Component {
  @Serial
  private static final long serialVersionUID = -1810768571143367371L;
  private float x;
  private float y;
  private float scaleX;
  private float scaleY;
  private String parentUniqueId = null;
  private TransformComponent parentTransformComponent = null;

  public TransformComponent() {
    this(0, 0);
  }

  public TransformComponent(float x, float y) {
    this(x, y, 1);
  }

  public TransformComponent(float x, float y, float scale) {
    this(x, y, scale, scale);
  }

  public TransformComponent(float x, float y, float scaleX, float scaleY) {
    this.x = x;
    this.y = y;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
  }

  public TransformComponent(Entity parent) {
    this(0, 0, parent);
  }

  public TransformComponent(float x, float y, Entity parent) {
    this(x, y, 1, parent);
  }

  public TransformComponent(float x, float y, float scale, Entity parent) {
    this(x, y, scale, scale, parent);
  }

  public TransformComponent(float x, float y, float scaleX, float scaleY, Entity parent) {
    this(x, y, scaleX, scaleY);

    setParent(parent);
  }

  @Override
  public void afterDeserialize(World world) {
    if (parentUniqueId != null) {
      world.findEntityByUniqueId(parentUniqueId).ifPresent(this::setParent);
    }
  }

  public float getX() {
    return parentTransformComponent == null ? x : x + parentTransformComponent.getX();
  }

  public float getY() {
    return parentTransformComponent == null ? y : y + parentTransformComponent.getY();
  }

  public Vector2D getTranslate() {
    return new Vector2D(getX(), getY());
  }

  public void setTranslate(Vector2D translate) {
    setX(translate.x());
    setY(translate.y());
  }

  public void setTranslate(float x, float y) {
    setX(x);
    setY(y);
  }

  public float getScaleX() {
    return parentTransformComponent == null ? scaleX : scaleX * parentTransformComponent.scaleX;
  }

  public float getScaleY() {
    return parentTransformComponent == null ? scaleY : scaleY * parentTransformComponent.scaleY;
  }

  public String getParentUniqueId() {
    return parentUniqueId;
  }

  public void setX(float x) {
    this.x = x;
  }

  public void setY(float y) {
    this.y = y;
  }

  public void setScale(float scaleX, float scaleY) {
    setScaleX(scaleX);
    setScaleY(scaleY);
  }

  public void setScale(float scale) {
    setScaleX(scale);
    setScaleY(scale);
  }

  public void setScaleX(float scaleX) {
    this.scaleX = scaleX;
  }

  public void setScaleY(float scaleY) {
    this.scaleY = scaleY;
  }

  public TransformComponent setParent(Entity entity) {
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
