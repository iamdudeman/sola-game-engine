package technology.sola.engine.graphics.components;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;
import technology.sola.math.geometry.ConvexPolygon;

/**
 * ConvexPolygonRendererComponent is a {@link Component} containing data for rendering 2d convex polygons.
 */
@NullMarked
public class ConvexPolygonRendererComponent implements Component {
  private Color color;
  private boolean isFilled;
  private final ConvexPolygon convexPolygon;

  /**
   * Creates a ConvexPolygonRendererComponent of desired color that is either filled or not filled.
   *
   * @param color         the {@link Color} of the convex polygon
   * @param isFilled      whether the convex polygon should be filled or not
   * @param convexPolygon the shape of the convex polygon to be rendered
   */
  public ConvexPolygonRendererComponent(Color color, boolean isFilled, ConvexPolygon convexPolygon) {
    this.color = color;
    this.isFilled = isFilled;
    this.convexPolygon = convexPolygon;
  }

  /**
   * @return the {@link Color} of the convex polygon
   */
  public Color getColor() {
    return color;
  }

  /**
   * @return true if the convex polygon should be filled when rendered
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
   * Sets whether the convex polygon should be filled or not.
   *
   * @param filled the new filled status
   */
  public void setFilled(boolean filled) {
    isFilled = filled;
  }

  /**
   * @return the shape of the convex polygon to be rendered
   */
  public ConvexPolygon getConvexPolygon() {
    return convexPolygon;
  }
}
