package technology.sola.engine.physics.component;

import technology.sola.engine.ecs.Component;
import technology.sola.math.linear.Vector2D;

public class PositionComponent implements Component {
  private Vector2D position;

  /**
   * Creates a PositionComponent at coordinates (0,0).
   */
  public PositionComponent() {
    this(0.0f, 0.0f);
  }

  /**
   * Creates a PositionComponent at the desired x and y coordinates.
   *
   * @param x  the x coordinate
   * @param y  the y coordinate
   */
  public PositionComponent(float x, float y) {
    position = new Vector2D(x, y);
  }

  /**
   * Gets the position as a {@link Vector2D}.
   *
   * @return the position as a {@code Vector2D}
   */
  public Vector2D get() {
    return position;
  }

  /**
   * Sets the position from a {@link Vector2D}.
   *
   * @param vector2D  the new position
   */
  public void set(Vector2D vector2D) {
    position = vector2D;
  }

  /**
   * Gets the x coordinate.
   *
   * @return the x coordinate
   */
  public float getX() {
    return position.x;
  }

  /**
   * Gets the y coordinate.
   *
   * @return the y coordinate
   */
  public float getY() {
    return position.y;
  }

  /**
   * Sets the x coordinate.
   *
   * @param x  the new x coordinate
   */
  public void setX(float x) {
    position = new Vector2D(x, position.y);
  }

  /**
   * Sets the y coordinate.
   *
   * @param y  the new y coordinate
   */
  public void setY(float y) {
    position = new Vector2D(position.x, y);
  }
}
