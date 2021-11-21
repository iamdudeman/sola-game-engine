package technology.sola.engine.core.component;

import technology.sola.engine.ecs.Component;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

public class TransformComponent implements Component {
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

  // TODO this isn't a very good method name nor good to have on TransformComponent
  // TODO test and cleanup more
  public TransformComponent apply(TransformComponent transformComponent) {
    var cameraScaleTransform = Matrix3D.scale(transformComponent.scaleX, transformComponent.scaleY);
    Vector2D scale = cameraScaleTransform.forward(scaleX, scaleY);
    var thisTranslation = cameraScaleTransform.forward(x, y);
    var otherTranslation = cameraScaleTransform.forward(transformComponent.x, transformComponent.y);

    var test = Matrix3D.translate(transformComponent.x, transformComponent.y)
      .multiply(cameraScaleTransform);

    thisTranslation = test.forward(x, y);

    return new TransformComponent(
      thisTranslation.x, thisTranslation.y,
//      thisTranslation.x - otherTranslation.x, thisTranslation.y - otherTranslation.y,
      scale.x, scale.y
    );
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
