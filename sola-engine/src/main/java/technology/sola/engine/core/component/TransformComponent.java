package technology.sola.engine.core.component;

import technology.sola.engine.ecs.Component;
import technology.sola.math.linear.Vector2D;

import java.io.Serial;

public class TransformComponent implements Component<TransformComponent> {
  @Serial
  private static final long serialVersionUID = -1810768571143367371L;
  private float x;
  private float y;
  private float scaleX;
  private float scaleY;

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

  @Override
  public TransformComponent copy() {
    return new TransformComponent(x, y, scaleX, scaleY);
  }

  /**
   * @return x coordinate of center of {@link technology.sola.engine.ecs.Entity}
   */
  public float getX() {
    return x;
  }

  /**
   * @return y coordinate of center of {@link technology.sola.engine.ecs.Entity}
   */
  public float getY() {
    return y;
  }

  public Vector2D getTranslate() {
    return new Vector2D(x, y);
  }

  public void setTranslate(Vector2D translate) {
    this.x = translate.x;
    this.y = translate.y;
  }

  public float getScaleX() {
    return scaleX;
  }

  public float getScaleY() {
    return scaleY;
  }

  public void setX(float x) {
    this.x = x;
  }

  public void setY(float y) {
    this.y = y;
  }

  public void setScaleX(float scaleX) {
    this.scaleX = scaleX;
  }

  public void setScaleY(float scaleY) {
    this.scaleY = scaleY;
  }
}