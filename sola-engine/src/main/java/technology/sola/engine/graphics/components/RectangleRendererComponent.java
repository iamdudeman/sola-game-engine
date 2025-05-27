package technology.sola.engine.graphics.components;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;

/**
 * RectangleRendererComponent is a {@link Component} containing data for rendering 2d rectangles.
 */
@NullMarked
public class RectangleRendererComponent implements Component {
  private Color color;
  private boolean isFilled;

  /**
   * Creates a RectangleRendererComponent of desired color that is filled.
   *
   * @param color the {@link Color} of the rectangle
   */
  public RectangleRendererComponent(Color color) {
    this(color, true);
  }

  /**
   * Creates a RectangleRendererComponent of desired color that is either filled or not filled.
   *
   * @param color    the {@link Color} of the rectangle
   * @param isFilled whether the rectangle should be filled or not
   */
  public RectangleRendererComponent(Color color, boolean isFilled) {
    this.color = color;
    this.isFilled = isFilled;
  }

  /**
   * @return the {@link Color} of the rectangle
   */
  public Color getColor() {
    return color;
  }

  /**
   * @return true if the rectangle should be filled when rendered
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
   * Sets whether the rectangle should be filled or not.
   *
   * @param filled the new filled status
   */
  public void setFilled(boolean filled) {
    isFilled = filled;
  }
}
