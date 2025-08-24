package technology.sola.engine.graphics.components;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

/**
 * TriangleRendererComponent is a {@link Component} containing data for rendering 2d triangles.
 */
@NullMarked
public class TriangleRendererComponent implements Component {
  private Color color;
  private boolean isFilled;
  private final Triangle triangle;

  /**
   * Creates a "unit triangle" renderer with uniform side lengths of 1 that is filled.
   *
   * @param color the {@link Color} of the triangle
   */
  public TriangleRendererComponent(Color color) {
    this(color, true);
  }

  /**
   * Creates a "unit triangle" renderer with uniform side lengths of 1.
   *
   * @param color    the {@link Color} of the triangle
   * @param isFilled whether the triangle should be filled or not
   */
  public TriangleRendererComponent(Color color, boolean isFilled) {
    this(color, isFilled, new Triangle(new Vector2D(0, 0), new Vector2D(0.5f, 1), new Vector2D(1, 0)));
  }

  /**
   * Creates a TriangleRendererComponent of desired color that is filled.
   *
   * @param color    the {@link Color} of the triangle
   * @param triangle the shape of the triangle to be rendered
   */
  public TriangleRendererComponent(Color color, Triangle triangle) {
    this(color, true, triangle);
  }

  /**
   * Creates a TriangleRendererComponent of desired color that is either filled or not filled.
   *
   * @param color    the {@link Color} of the triangle
   * @param isFilled whether the triangle should be filled or not
   * @param triangle the shape of the triangle to be rendered
   */
  public TriangleRendererComponent(Color color, boolean isFilled, Triangle triangle) {
    this.color = color;
    this.isFilled = isFilled;
    this.triangle = triangle;
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
   * @return the shape of the triangle to be rendered
   */
  public Triangle getTriangle() {
    return triangle;
  }
}
