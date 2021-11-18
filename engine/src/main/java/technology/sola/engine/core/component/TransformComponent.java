package technology.sola.engine.core.component;

import technology.sola.engine.ecs.Component;
import technology.sola.math.linear.Matrix3D;

public class TransformComponent implements Component {
  private float x;
  private float y;
  private float scaleX;
  private float scaleY;
  private float rotation;
  private transient Matrix3D transform;

  public TransformComponent() {
    this(0, 0);
  }

  public TransformComponent(float x, float y) {
    this(x, y, 1, 1, 0);
  }

  public TransformComponent(float x, float y, float scaleX, float scaleY, float rotation) {
    this.x = x;
    this.y = y;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    this.rotation = rotation;

    transform = Matrix3D.translate(x, y)
      .multiply(Matrix3D.rotate(rotation)) // TODO consider removing rotation
      .multiply(Matrix3D.scale(scaleX, scaleY));
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

  public float getScaleX() {
    return scaleX;
  }

  public float getScaleY() {
    return scaleY;
  }

  public float getRotation() {
    return rotation;
  }

  public Matrix3D getTransform() {
    return transform;
  }
}
