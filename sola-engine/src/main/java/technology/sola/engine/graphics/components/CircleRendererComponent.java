package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;

/**
 * CircleRendererComponent is a {@link Component} containing data for rendering 2d circles.
 */
public class CircleRendererComponent implements Component {
  private Color color;
  private boolean isFilled;

  /**
   * Creates a CircleRendererComponent of desired color that is filled.
   *
   * @param color the {@link Color} of the circle
   */
  public CircleRendererComponent(Color color) {
    this(color, true);
  }

  /**
   * Creates a CircleRendererComponent of desired color that is either filled or not filled.
   *
   * @param color    the {@link Color} of the circle
   * @param isFilled whether the circle should be filled or not
   */
  public CircleRendererComponent(Color color, boolean isFilled) {
    this.color = color;
    this.isFilled = isFilled;
  }

  /**
   * @return the {@link Color} of the circle
   */
  public Color getColor() {
    return color;
  }

  /**
   * @return true if the circle should be filled when rendered
   */
  public boolean isFilled() {
    return isFilled;
  }

  /**
   * Sets the {@link Color} for rendering.
   *
   * @param color the new color
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Sets whether the circle should be filled or not.
   *
   * @param filled the new filled status
   */
  public void setFilled(boolean filled) {
    isFilled = filled;
  }
}
