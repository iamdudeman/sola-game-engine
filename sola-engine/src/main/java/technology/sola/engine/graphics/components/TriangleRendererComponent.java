package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;
import technology.sola.math.linear.Vector2D;

/**
 * TriangleRendererComponent is a {@link Component} containing data for rendering 2d triangles.
 */
public class TriangleRendererComponent implements Component {
  private Color color;
  private boolean isFilled;

  /**
   * Creates a TriangleRendererComponent of desired color that is filled.
   *
   * @param color the {@link Color} of the triangle
   */
  public TriangleRendererComponent(Color color) {
    this(color, true);
  }

  /**
   * Creates a TriangleRendererComponent of desired color that is either filled or not filled.
   *
   * @param color    the {@link Color} of the triangle
   * @param isFilled whether the triangle should be filled or not
   */
  public TriangleRendererComponent(Color color, boolean isFilled) {
    this.color = color;
    this.isFilled = isFilled;
  }

  /**
   * @return the {@link Color} of the triangle
   */
  public Color getColor() {
    return color;
  }

  /**
   * @return true if the triangle should be filled when rendered
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
   * Sets whether the triangle should be filled or not.
   *
   * @param filled the new filled status
   */
  public void setFilled(boolean filled) {
    isFilled = filled;
  }
}
