package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;

/**
 * TriangleRendererComponent is a {@link Component} containing data for rendering 2d triangles.
 */
public class TriangleRendererComponent implements Component {
  private final float verticalPointOffsetPercentage;
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
    this(color, isFilled, 0.5f);
  }

  /**
   * Creates a TriangleRendererComponent of desired color that is either filled or not filled. A vertical point offset
   * percentage can be set which modifies where it appears on the horizontal access. 0.5 is midway, 0 and 1 is all the
   * way left or right respectively creating a right triangle.
   *
   * @param color                         the {@link Color} of the triangle
   * @param isFilled                      whether the triangle should be filled or not
   * @param verticalPointOffsetPercentage the offset percentage of the vertical most point
   */
  public TriangleRendererComponent(Color color, boolean isFilled, float verticalPointOffsetPercentage) {
    this.color = color;
    this.isFilled = isFilled;
    this.verticalPointOffsetPercentage = verticalPointOffsetPercentage;

    if (verticalPointOffsetPercentage < 0 || verticalPointOffsetPercentage > 1) {
      throw new IllegalArgumentException("verticalPointOffsetPercentage must be between 0 and 1");
    }
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

  /**
   * The offset percentage of the vertical point. This modifies where it appears on the horizontal access. 0.5 is
   * midway, 0 and 1 is all the way left or right respectively creating a right triangle.
   *
   * @return the vertical point offset percentage
   */
  public float getVerticalPointOffsetPercentage() {
    return verticalPointOffsetPercentage;
  }
}
