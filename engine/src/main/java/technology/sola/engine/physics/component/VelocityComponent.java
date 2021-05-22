package technology.sola.engine.physics.component;

import technology.sola.engine.ecs.Component;
import technology.sola.math.SolKanaMath;
import technology.sola.math.linear.Vector2D;

public class VelocityComponent implements Component {
  private Vector2D velocity;
  private Float maxVelocityX = null;

  /**
   * Creates a VelocityComponent at rest.
   */
  public VelocityComponent() {
    this(0.0f, 0.0f);
  }

  /**
   * Creates a VelocityComponent with set x and y velocities.
   *
   * @param x  velocity on horizontal axis
   * @param y  velocity on vertical axis
   */
  public VelocityComponent(float x, float y) {
    this(new Vector2D(x, y));
  }

  /**
   * Creates A VelocityComponent from a {@link Vector2D}.
   *
   * @param velocity  the {@code Vector2D} representing the velocity
   */
  public VelocityComponent(Vector2D velocity) {
    this.velocity = velocity;
  }

  /**
   * Gets velocity as a {@link Vector2D}.
   *
   * @return velocity as a vector
   */
  public Vector2D get() {
    return velocity;
  }

  /**
   * Sets velocity from a vector.
   *
   * @param vector2D  vector to set velocity from
   */
  public void set(Vector2D vector2D) {
    float velocityX = vector2D.x;
    float velocityY = vector2D.y;

    if (maxVelocityX != null) {
      velocityX = SolKanaMath.clamp(-maxVelocityX, maxVelocityX, vector2D.x);
    }

    velocity = new Vector2D(velocityX, velocityY);
  }

  /**
   * Gets the velocity on the horizontal axis.
   *
   * @return velocity on horizontal axis
   */
  public float getX() {
    return velocity.x;
  }

  /**
   * Gets the velocity on the vertical axis.
   *
   * @return velocity on vertical axis
   */
  public float getY() {
    return velocity.y;
  }

  /**
   * Sets velocity on horizontal axis.
   *
   * @param x  new horizontal axis velocity
   */
  public void setX(float x) {
    set(new Vector2D(x, velocity.y));
  }

  /**
   * Sets velocity on vertical axis.
   *
   * @param y  new vertical axis velocity
   */
  public void setY(float y) {
    set(new Vector2D(velocity.x, y));
  }

  /**
   * Sets a max velocity on the horizontal axis (both positive and negative). If a velocity is set larger than
   * this value in the positive or negative direction the positive or negative maxVelocityX value will be used instead.
   *
   * @param maxVelocityX  The maximum velocity in the horizontal direction
   */
  public void setMaxVelocityX(Float maxVelocityX) {
    this.maxVelocityX = maxVelocityX;
  }
}
